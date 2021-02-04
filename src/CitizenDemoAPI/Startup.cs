using CitizenDemo.CitizenDemoAPI.Data;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.HttpsPolicy;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;
using Microsoft.Identity.Web;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CitizenDemo.CitizenDemoAPI
{
    public class Startup
    {
        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        public IConfiguration Configuration { get; }

        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services)
        {
            services.AddSingleton<IDatabaseSettings>(new DatabaseSettings
            {
                ConnectionString = Configuration.GetSection("DatabaseSettings")["ConnectionString"],
                DatabaseName = Configuration.GetSection("DatabaseSettings")["DatabaseName"],
                CitizenCollectionName = Configuration.GetSection("DatabaseSettings")["CitizenCollectionName"]
            });
            services.AddSingleton<ICitizenRepository, CitizenRepository>();

            services.AddControllers();

            services.AddMicrosoftIdentityWebApiAuthentication(Configuration)
                .EnableTokenAcquisitionToCallDownstreamApi()
                .AddInMemoryTokenCaches();
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IWebHostEnvironment env)
        {
            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
            }

            app.UseHttpsRedirection();

            app.UseRouting();

            app.UseAuthentication();

            app.UseAuthorization();

            app.UseEndpoints(endpoints =>
            {
                endpoints.MapControllers();
            });
        }
    }
}
