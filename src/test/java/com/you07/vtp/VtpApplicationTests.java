package com.you07.vtp;

import com.you07.vtp.dao.LocationLatestDao;
import com.you07.vtp.model.LocationLatest;
import com.you07.vtp.service.LocationLatestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class VtpApplicationTests {

	@Autowired
	private LocationLatestService locationLatestService;

	@Resource
	private LocationLatestDao locationLatestDao;

	@Test
	public void contextLoads() {
//		locationCampusInfoService.initCampus();
		LocationLatest locationLatest = locationLatestDao.selectByPrimaryKey("2017223030056");
		locationLatestService.saveLocation(locationLatest);
	}

}
