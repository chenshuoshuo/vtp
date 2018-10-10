package com.you07;

import com.you07.map.model.MapZone;
import com.you07.map.service.MapZoneService;
import com.you07.vtp.model.LocationCampusInfo;
import com.you07.vtp.model.LocationSystemConfig;
import com.you07.vtp.service.LocationCampusInfoService;
import com.you07.vtp.service.LocationSystemConfigService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Date;
import java.util.List;


@tk.mybatis.spring.annotation.MapperScan(
		basePackages = {"com.you07.eas.dao",
				"com.you07.vtp.dao",
				"com.you07.map.dao"})
@SpringBootApplication
@EnableScheduling
public class VtpApplication implements ApplicationListener<ApplicationEvent> {

	public static void main(String[] args) {
		SpringApplication.run(VtpApplication.class, args);
	}

	@Override
	public void onApplicationEvent(ApplicationEvent applicationEvent) {
		if (applicationEvent instanceof ApplicationReadyEvent){
			// 从上下文获取会用到的bean
			ConfigurableApplicationContext applicationContext = ((ApplicationReadyEvent) applicationEvent).getApplicationContext();
			// 地图区域信息的service
			MapZoneService mapZoneService = applicationContext.getBean(MapZoneService.class);
			// 可视化轨迹平台校区信息service
			LocationCampusInfoService locationCampusInfoService = applicationContext.getBean(LocationCampusInfoService.class);
			// 配置信息service
			LocationSystemConfigService locationSystemConfigService = applicationContext.getBean(LocationSystemConfigService.class);

			if(locationSystemConfigService.loadDefault() == null){

				LocationSystemConfig locationSystemConfig = new LocationSystemConfig();
				locationSystemConfig.setConfigId(1);
				locationSystemConfig.setSystemName("可视化轨迹平台");
				locationSystemConfig.setUpdateTime(new Date());
				locationSystemConfigService.add(locationSystemConfig);

				List<MapZone> mapZoneList = mapZoneService.query2dMap();

				for(MapZone mapZone : mapZoneList){
					LocationCampusInfo locationCampusInfo = new LocationCampusInfo();
					locationCampusInfo.setCampusId(mapZone.getZoneid());
					locationCampusInfo.setCampusName(mapZone.getName());
					locationCampusInfo.setIsDefault(0);
					locationCampusInfo.setLeftDownLat(mapZone.getLeftBottomLat());
					locationCampusInfo.setLeftDownLng(mapZone.getLeftBottomLon());
					locationCampusInfo.setRightTopLat(mapZone.getRightTopLat());
					locationCampusInfo.setRightTopLng(mapZone.getRightTopLon());
					locationCampusInfo.setIsDisplay(1);

					locationCampusInfoService.add(locationCampusInfo);
				}
			}
		}
	}
}
