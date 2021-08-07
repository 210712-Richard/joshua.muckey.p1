package com.revature.models;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class Ticket {
	
	private String user;
	private BasicInfo info;
	private LocalDateTime date;
	private String location;
	private Double cost;
	private GradeType gradeType;
	private String justification;
	private List<Attachment> attachments;
	private Period missedTime;
	private TicketStatus status;
	
	public Ticket(String user, BasicInfo info, LocalDateTime date, String location, Double cost, GradeType gradeType,
			String justification, List<Attachment> attachments, Period missedTime) {
		super();
		status = TicketStatus.SUPER;
		this.user = user;
		this.info = info;
		this.date = date;
		this.location = location;
		this.cost = cost;
		this.gradeType = gradeType;
		this.justification = justification;
		this.attachments = attachments;
		this.missedTime = missedTime;
	}

	public TicketStatus getStatus() {
		return status;
	}

	public void setStatus(TicketStatus status) {
		this.status = status;
	}

	public BasicInfo getInfo() {
		return info;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setInfo(BasicInfo info) {
		this.info = info;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public GradeType getGradeType() {
		return gradeType;
	}

	public void setGradeType(GradeType gradeType) {
		this.gradeType = gradeType;
	}

	public String getJustification() {
		return justification;
	}

	public void setJustification(String justification) {
		this.justification = justification;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}
	public List<UUID> getAttachmentsId(){
		return attachments.stream().map((m)->m.getId()).collect(Collectors.toList());
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	public Period getMissedTime() {
		return missedTime;
	}

	public void setMissedTime(Period missedTime) {
		this.missedTime = missedTime;
	}

	@Override
	public int hashCode() {
		return Objects.hash(attachments, cost, date, gradeType, info, justification, location, missedTime, status,
				user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ticket other = (Ticket) obj;
		return Objects.equals(attachments, other.attachments) && Objects.equals(cost, other.cost)
				&& Objects.equals(date, other.date) && gradeType == other.gradeType && Objects.equals(info, other.info)
				&& Objects.equals(justification, other.justification) && Objects.equals(location, other.location)
				&& Objects.equals(missedTime, other.missedTime) && status == other.status
				&& Objects.equals(user, other.user);
	}

	@Override
	public String toString() {
		return "Ticket [user=" + user + ", info=" + info + ", date=" + date + ", location=" + location + ", cost="
				+ cost + ", gradeType=" + gradeType + ", justification=" + justification + ", attachments="
				+ attachments + ", missedTime=" + missedTime + ", status=" + status + "]";
	}
	

}
