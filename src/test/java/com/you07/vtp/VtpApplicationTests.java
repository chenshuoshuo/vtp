package com.you07.vtp;

import com.you07.util.RestTemplateUtil;
import com.you07.vtp.controller.LocationController;
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

	@Autowired
	private LocationController locationController;

	@Test
	public void contextLoads() {
		String str = locationController.loadTrack("zx240016", "2019-06-29 00:00:00", "2019-06-30 23:00:00", 1, 24, null);
		System.out.println(str);
	}

}
