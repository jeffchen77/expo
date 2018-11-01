package com.xiaoi.expo.management.dialogue.meeting.dto;

import java.util.List;

import com.xiaoi.expo.management.baseconfig.enterprise.entity.Enterprise;
import com.xiaoi.expo.management.baseconfig.guest.entity.Guest;
import com.xiaoi.expo.management.dialogue.meeting.entity.Meeting;

public class MeetingDto extends Meeting {

	
	private String rang; //时间范围
	private String status; //状态
	private List<String> guestArr;
	
	public String getRang() {
		return rang;
	}
	public void setRang(String rang) {
		this.rang = rang;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<String> getGuestArr() {
		return guestArr;
	}
	public void setGuestArr(List<String> guestArr) {
		this.guestArr = guestArr;
	}
	
	
	
}
