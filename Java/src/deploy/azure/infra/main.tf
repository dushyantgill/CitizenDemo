data "azurerm_client_config" "current" { }
resource "azurerm_resource_group" "rg" {
  name     = "citizendemo"
  location = "East US"
}

resource "azurerm_kubernetes_cluster" "aks" {
  name                = "citizendemoaks"
  location            = azurerm_resource_group.rg.location
  resource_group_name = azurerm_resource_group.rg.name
  node_resource_group = "citizendemo-infra"
  dns_prefix          = "citizendemoaks"
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
  name                = "citizendemoacr"
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
  name     = "citizendemo-monitor"
  location = "East US"
}

resource "azurerm_log_analytics_workspace" "logs" {
  name                = "citizendemologs"
  location            = azurerm_resource_group.rg-monitor.location
  resource_group_name = azurerm_resource_group.rg-monitor.name
  sku                 = "PerGB2018"
}

resource "azurerm_monitor_workspace" "metrics" {
  name                = "citizendemometrics"
  location            = azurerm_resource_group.rg-monitor.location
  resource_group_name = azurerm_resource_group.rg-monitor.name
}
resource "azurerm_monitor_data_collection_endpoint" "metrics-dce" {
  name                = "citizendemometricsdce"
  location            = azurerm_resource_group.rg-monitor.location
  resource_group_name = azurerm_resource_group.rg-monitor.name
  kind                = "Linux"
}
resource "azurerm_monitor_data_collection_rule" "metrics-dcr" {
  name                        = "citizendemometricsdcr"
  location                    = azurerm_resource_group.rg-monitor.location
  resource_group_name         = azurerm_resource_group.rg-monitor.name
  data_collection_endpoint_id = azurerm_monitor_data_collection_endpoint.metrics-dce.id
  kind                        = "Linux"
  destinations {
    monitor_account {
      monitor_account_id = azurerm_monitor_workspace.metrics.id
      name               = "citizendemometrics"
    }
  }
  data_flow {
    streams      = ["Microsoft-PrometheusMetrics"]
    destinations = ["citizendemometrics"]
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
  name                    = "citizendemometricsdcra"
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
  name                = "citizendemotraces"
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
  name                = "citizendemodashboards"
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