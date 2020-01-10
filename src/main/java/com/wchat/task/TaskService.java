package com.wchat.task;

import com.wchat.bean.VerifiCodeCacheBean;
import com.wchat.socket.WebSocketServer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;


@Component
@EnableScheduling
public class TaskService {

	@Scheduled(cron = "30 * * * * ? ")
	public void run() throws InterruptedException {
		Map<String, String> map = VerifiCodeCacheBean.getCache().getMap();
		int baseNum = getBaseNum();
		if(map.size()>=baseNum){
			clearHalf();
		}

	}

	private void clearHalf() {
		Map<String, String> map = VerifiCodeCacheBean.getCache().getMap();
		LinkedHashMap<String, String> newMap = new LinkedHashMap<>(map.size()/2);
		int be = 0;

		for(Map.Entry<String, String> entry : map.entrySet()) {
			if(be>map.size()/2){
				newMap.put(entry.getKey(),entry.getValue());
			}
			be++;
		}
	}

	private int getBaseNum(){
		int socketSize = WebSocketServer.SessionSet.size();
		return socketSize<50?100:socketSize*2;
	}
}
