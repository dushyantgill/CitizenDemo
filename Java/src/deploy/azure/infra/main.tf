data "azurerm_client_config" "current" { }
resource "random_id" "suffix" {
  byte_length = 2
}

resource "azurerm_resource_group" "rg" {
  name     = "citizendemo${random_id.suffix.hex}"
  location = "East US"
}

resource "azurerm_kubernetes_cluster" "aks" {
  name                = "citizendemo${random_id.suffix.hex}aks"
  location            = azurerm_resource_group.rg.location
  resource_group_name = azurerm_resource_group.rg.name
  node_resource_group = "citizendemo${random_id.suffix.hex}-infra"
  dns_prefix          = "citizendemo${random_id.suffix.hex}aks"
  default_node_pool {
    name       = "default"
    node_count = 3
    vm_size    = "Standard_D2_v2"
  }
  identity {
    type = "SystemAssigned"
  }
  oms_agent {
    log_analytics_workspace_id = azurerm_log_analytics_workspace.logs.id
  }
  monitor_metrics {
    annotations_allowed = null
    labels_allowed      = null
  }
}

resource "azurerm_container_registry" "acr" {
  name                = "citizendemo${random_id.suffix.hex}acr"
  location            = azurerm_resource_group.rg.location
  resource_group_name = azurerm_resource_group.rg.name
  sku                 = "Standard"
  admin_enabled       = false
}
resource "azurerm_role_assignment" "aks-acrpull-role-assignment" {
  principal_id                     = azurerm_kubernetes_cluster.aks.kubelet_identity[0].object_id
  role_definition_name             = "AcrPull"
  scope                            = azurerm_container_registry.acr.id
  skip_service_principal_aad_check = true
}
resource "azurerm_role_assignment" "current-user-acr-contributor-role-assignment" {
  principal_id                     = data.azurerm_client_config.current.object_id
  role_definition_name             = "Contributor"
  scope                            = azurerm_container_registry.acr.id
}

resource "azurerm_resource_group" "rg-monitor" {
  name     = "citizendemo${random_id.suffix.hex}-monitor"
  location = "East US"
}

resource "azurerm_log_analytics_workspace" "logs" {
  name                = "citizendemo${random_id.suffix.hex}logs"
  location            = azurerm_resource_group.rg-monitor.location
  resource_group_name = azurerm_resource_group.rg-monitor.name
  sku                 = "PerGB2018"
}

resource "azurerm_monitor_workspace" "metrics" {
  name                = "citizendemo${random_id.suffix.hex}metrics"
  location            = azurerm_resource_group.rg-monitor.location
  resource_group_name = azurerm_resource_group.rg-monitor.name
}
resource "azurerm_monitor_data_collection_endpoint" "metrics-dce" {
  name                = "citizendemo${random_id.suffix.hex}metricsdce"
  location            = azurerm_resource_group.rg-monitor.location
  resource_group_name = azurerm_resource_group.rg-monitor.name
  kind                = "Linux"
}
resource "azurerm_monitor_data_collection_rule" "metrics-dcr" {
  name                        = "citizendemo${random_id.suffix.hex}metricsdcr"
  location                    = azurerm_resource_group.rg-monitor.location
  resource_group_name         = azurerm_resource_group.rg-monitor.name
  data_collection_endpoint_id = azurerm_monitor_data_collection_endpoint.metrics-dce.id
  kind                        = "Linux"
  destinations {
    monitor_account {
      monitor_account_id = azurerm_monitor_workspace.metrics.id
      name               = "citizendemo${random_id.suffix.hex}metrics"
    }
  }
  data_flow {
    streams      = ["Microsoft-PrometheusMetrics"]
    destinations = ["citizendemo${random_id.suffix.hex}metrics"]
  }
  data_sources {
    prometheus_forwarder {
      streams = ["Microsoft-PrometheusMetrics"]
      name    = "PrometheusDataSource"
    }
  }
  depends_on = [
    azurerm_monitor_data_collection_endpoint.metrics-dce
  ]
}
resource "azurerm_monitor_data_collection_rule_association" "metrics-dcra" {
  name                    = "citizendemo${random_id.suffix.hex}metricsdcra"
  target_resource_id      = azurerm_kubernetes_cluster.aks.id
  data_collection_rule_id = azurerm_monitor_data_collection_rule.metrics-dcr.id
  depends_on = [
    azurerm_monitor_data_collection_rule.metrics-dcr
  ]
}
resource "azurerm_role_assignment" "metrics-datareader-role-assignment" {
  principal_id                     = data.azurerm_client_config.current.object_id
  role_definition_name             = "Monitoring Data Reader"
  scope                            = azurerm_monitor_workspace.metrics.id
}

resource "azurerm_application_insights" "traces" {
  name                = "citizendemo${random_id.suffix.hex}traces"
  location            = azurerm_resource_group.rg-monitor.location
  resource_group_name = azurerm_resource_group.rg-monitor.name
  workspace_id        = azurerm_log_analytics_workspace.logs.id
  application_type    = "web"
}
output "traces-connection-string" {
  value = azurerm_application_insights.traces.connection_string
  sensitive = true
}

resource "azurerm_dashboard_grafana" "grafana" {
  name                = "citizendemo${random_id.suffix.hex}dashboards"
  location            = azurerm_resource_group.rg-monitor.location
  resource_group_name = azurerm_resource_group.rg-monitor.name
  identity {
    type = "SystemAssigned"
  }
  azure_monitor_workspace_integrations {
    resource_id = azurerm_monitor_workspace.metrics.id
  }
}
resource "azurerm_role_assignment" "grafana-datareader-role-assignment" {
  principal_id                     = azurerm_dashboard_grafana.grafana.identity.0.principal_id
  role_definition_name             = "Monitoring Data Reader"
  scope                            = azurerm_monitor_workspace.metrics.id
  skip_service_principal_aad_check = true
}
resource "azurerm_role_assignment" "grafanaadmin-role-assignment" {
  principal_id                     = data.azurerm_client_config.current.object_id
  role_definition_name             = "Grafana Admin"
  scope                            = azurerm_dashboard_grafana.grafana.id
}