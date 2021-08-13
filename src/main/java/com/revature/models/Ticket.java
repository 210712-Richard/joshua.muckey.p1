package com.revature.models;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Ticket {
	
	private String user;
	private UUID id;
	private BasicInfo info;
	@JsonFormat(pattern="dd-MM-yyyy")
	private LocalDate date;
	private String location;
	private Double cost;
	private Department dept;
	private GradeType gradeType;
	private String justification;
	private List<UUID> attachments = new ArrayList<UUID>();
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd")
	private Period missedTime;
	private TicketStatus status;
	
	public Ticket(String user, BasicInfo info, LocalDate date, String location,Department dept, Double cost, GradeType gradeType,
			String justification, List<UUID> attachments, Period missedTime, UUID id) {
		super();
		status = TicketStatus.SUPER;
		this.id = id;
		this.dept = dept;
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
	public Ticket(String user, BasicInfo info, LocalDate date, String location,Department dept, Double cost, GradeType gradeType,
			String justification, List<UUID> attachments, Period missedTime, UUID id, TicketStatus status) {
		super();
		this.status = status;
		this.id = id;
		this.dept = dept;
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
	public Ticket() {
		id = UUID.randomUUID();
	}
	
	public Department getDept() {
		return dept;
	}
	public void setDept(Department dept) {
		this.dept = dept;
	}
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
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

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
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

	public List<Attachment> getFiles() {
		return null;
	}
	public List<UUID> getAttachments(){
		return attachments;
	}

	public void setAttachments(List<UUID> attachments) {
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
		return Objects.hash(attachments, cost, date, dept, gradeType, id, info, justification, location, missedTime,
				status, user);
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
				&& Objects.equals(date, other.date) && dept == other.dept && gradeType == other.gradeType
				&& Objects.equals(id, other.id) && Objects.equals(info, other.info)
				&& Objects.equals(justification, other.justification) && Objects.equals(location, other.location)
				&& Objects.equals(missedTime, other.missedTime) && status == other.status
				&& Objects.equals(user, other.user);
	}

	@Override
	public String toString() {
		return "Ticket [user=" + user + ", id=" + id + ", info=" + info + ", date=" + date + ", location=" + location
				+ ", cost=" + cost + ", dept=" + dept + ", gradeType=" + gradeType + ", justification=" + justification
				+ ", attachments=" + attachments + ", missedTime=" + missedTime + ", status=" + status + "]";
	}
	

}
