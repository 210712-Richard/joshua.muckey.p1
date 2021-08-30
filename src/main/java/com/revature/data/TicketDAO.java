package com.revature.data;

import java.io.InputStream;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.DefaultConsistencyLevel;
import com.datastax.oss.driver.api.core.cql.BatchStatement;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.DefaultBatchType;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatementBuilder;
import com.revature.models.BasicInfo;
import com.revature.models.Department;
import com.revature.models.GradeType;
import com.revature.models.Ticket;
import com.revature.models.TicketStatus;
import com.revature.util.CassandraUtil;
import com.revature.util.S3Util;

public class TicketDAO {

	private CqlSession session = CassandraUtil.getInstance().getSession();

	public Ticket createSuper(Ticket ticket, String supervisor, Department dept) {

		BoundStatement b1 = basicCreate(ticket);
		BoundStatement b3 = timeCreate(ticket, null, supervisor);

		String q2 = "Insert into supertickets (supervisor, dept ,username, email, firstname, lastname, date, location, cost, gradetype,  justification, attachmentuuids, missed, ticketstatus, id) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

		SimpleStatement s2 = new SimpleStatementBuilder(q2).setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM)
				.build();

		BoundStatement b2 = session.prepare(s2).bind(supervisor, dept.name(), ticket.getUser(),
				ticket.getInfo().getEmail(), ticket.getInfo().getFirstName(), ticket.getInfo().getLastName(),
				ticket.getDate(), ticket.getLocation(), ticket.getCost(), ticket.getGradeType().name(),
				ticket.getJustification(), ticket.getAttachments(), ticket.getMissedTime().getDays(),
				ticket.getStatus().name(), ticket.getId());

		BatchStatement batch = BatchStatement.builder(DefaultBatchType.UNLOGGED)
				.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).addStatement(b1).addStatement(b2)
				.addStatement(b3).build();
		session.execute(batch);

		return ticket;
	}

	public Ticket createHead(Ticket ticket, Department dept) {

		BoundStatement b1 = basicCreate(ticket);
		BoundStatement b3 = timeCreate(ticket, dept, null);

		String q2 = "Insert into headtickets (dept, username, email, firstname, lastname, date, location, cost, gradetype, justification, attachmentuuids, missed, ticketstatus, id) values (?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

		SimpleStatement s2 = new SimpleStatementBuilder(q2).setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM)
				.build();

		BoundStatement b2 = session.prepare(s2).bind(dept.name(), ticket.getUser(), ticket.getInfo().getEmail(),
				ticket.getInfo().getFirstName(), ticket.getInfo().getLastName(), ticket.getDate(), ticket.getLocation(),
				ticket.getCost(), ticket.getGradeType().name(), ticket.getJustification(), ticket.getAttachments(),
				ticket.getMissedTime().getDays(), ticket.getStatus().name(), ticket.getId());

		BatchStatement batch = BatchStatement.builder(DefaultBatchType.UNLOGGED)
				.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).addStatement(b1).addStatement(b2)
				.addStatement(b3).build();
		session.execute(batch);

		return ticket;
	}

	private BoundStatement basicCreate(Ticket ticket) {
		String q1 = "Insert into tickets (username, email, firstname, lastname, date, location, cost, gradetype, justification, attachmentuuids, missed, ticketstatus, id, dept) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

		SimpleStatement s1 = new SimpleStatementBuilder(q1).setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM)
				.build();

		BoundStatement b1 = session.prepare(s1).bind(ticket.getUser(), ticket.getInfo().getEmail(),
				ticket.getInfo().getFirstName(), ticket.getInfo().getLastName(), ticket.getDate(), ticket.getLocation(),
				ticket.getCost(), ticket.getGradeType().name(), ticket.getJustification(), ticket.getAttachments(),
				ticket.getMissedTime().getDays(), ticket.getStatus().name(), ticket.getId(), ticket.getDept().name());
		return b1;
	}

	private BoundStatement timeCreate(Ticket ticket, Department dept, String supervisor) {
		if (dept != null) {
			String q1 = "Insert into timedtickets (timeinsert, dept, username, ticketstatus, id) values (toTimeStamp(now()), ?, ?, ?, ?);";
			SimpleStatement s1 = new SimpleStatementBuilder(q1)
					.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
			BoundStatement b1 = session.prepare(s1).bind(dept.name(), ticket.getUser(), ticket.getStatus().name(),
					ticket.getId());
			return b1;
		} else {

			String q1 = "Insert into timedtickets (timeinsert, supervisor, username, ticketstatus, id) values (toTimeStamp(now()), ?, ?, ?, ?);";
			SimpleStatement s1 = new SimpleStatementBuilder(q1)
					.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
			BoundStatement b1 = session.prepare(s1).bind(supervisor, ticket.getUser(), ticket.getStatus().name(),
					ticket.getId());
			return b1;
		}
	}

	public List<Ticket> getMyTickets(String username) {
		String q1 = "Select * from tickets where username=?";

		SimpleStatement s1 = new SimpleStatementBuilder(q1).build();
		BoundStatement b1 = session.prepare(s1).bind(username);
		List<Ticket> tickets = new ArrayList<Ticket>();
		ResultSet rs = session.execute(b1);
		rs.forEach(row -> {
			Ticket t = new Ticket(row.getString("username"),
					new BasicInfo(row.getString("firstname"), row.getString("lastname"), row.getString("email")),
					row.getLocalDate("date"), row.getString("location"), Department.valueOf(row.getString("dept")),
					row.getDouble("cost"), GradeType.valueOf(row.getString("gradetype")),
					row.getString("justification"), row.getList("attachmentuuids", UUID.class),
					Period.ofDays(row.getInt("missed")), row.getUuid("id"), TicketStatus.valueOf(row.getString("ticketstatus")));
			tickets.add(t);
		});

		return tickets;
	}

	public List<Ticket> getSuperTickets(String supervisor) {

		String q1 = "Select * from supertickets where supervisor=?";

		SimpleStatement s1 = new SimpleStatementBuilder(q1).build();
		BoundStatement b1 = session.prepare(s1).bind(supervisor);
		List<Ticket> tickets = new ArrayList<Ticket>();
		ResultSet rs = session.execute(b1);
		rs.forEach(row -> {
			Ticket t = new Ticket(row.getString("username"),
					new BasicInfo(row.getString("firstname"), row.getString("lastname"), row.getString("email")),
					row.getLocalDate("date"), row.getString("location"), Department.valueOf(row.getString("dept")),
					row.getDouble("cost"), GradeType.valueOf(row.getString("gradetype")),
					row.getString("justification"), row.getList("attachmentuuids", UUID.class),
					Period.ofDays(row.getInt("missed")), row.getUuid("id"));
			tickets.add(t);
		});
		return tickets;
	}

	public List<Ticket> getHeadTickets(Department dept) {
		String q1 = "Select * from headtickets where dept=?";

		SimpleStatement s1 = new SimpleStatementBuilder(q1).build();
		BoundStatement b1 = session.prepare(s1).bind(dept.name());
		List<Ticket> tickets = new ArrayList<Ticket>();
		ResultSet rs = session.execute(b1);
		rs.forEach(row -> {
			Ticket t = new Ticket(row.getString("username"),
					new BasicInfo(row.getString("firstname"), row.getString("lastname"), row.getString("email")),
					row.getLocalDate("date"), row.getString("location"), Department.valueOf(row.getString("dept")),
					row.getDouble("cost"), GradeType.valueOf(row.getString("gradetype")),
					row.getString("justification"), row.getList("attachmentuuids", UUID.class),
					Period.ofDays(row.getInt("missed")), row.getUuid("id"));
			t.setStatus(TicketStatus.HEAD);
			tickets.add(t);
		});
		return tickets;
	}

	public List<Ticket> getBenCoTickets() {
		String q1 = "Select * from bencotickets";

		SimpleStatement s1 = new SimpleStatementBuilder(q1).build();
		List<Ticket> tickets = new ArrayList<Ticket>();
		ResultSet rs = session.execute(s1);
		rs.forEach(row -> {
			Ticket t = new Ticket(row.getString("username"),
					new BasicInfo(row.getString("firstname"), row.getString("lastname"), row.getString("email")),
					row.getLocalDate("date"), row.getString("location"), Department.valueOf(row.getString("dept")),
					row.getDouble("cost"), GradeType.valueOf(row.getString("gradetype")),
					row.getString("justification"), row.getList("attachmentuuids", UUID.class),
					Period.ofDays(row.getInt("missed")), row.getUuid("id"), TicketStatus.valueOf(row.getString("ticketstatus")));
			tickets.add(t);
		});
		return tickets;
	}

	public Ticket approveSuperTicket(String supervisor, String id) {
		String q1 = "Select * from supertickets where supervisor=?";

		SimpleStatement s1 = new SimpleStatementBuilder(q1).build();
		BoundStatement b1 = session.prepare(s1).bind(supervisor);
		List<Ticket> tickets = new ArrayList<Ticket>();
		ResultSet rs = session.execute(b1);
		rs.forEach(row -> {
			Ticket t = new Ticket(row.getString("username"),
					new BasicInfo(row.getString("firstname"), row.getString("lastname"), row.getString("email")),
					row.getLocalDate("date"), row.getString("location"), Department.valueOf(row.getString("dept")),
					row.getDouble("cost"), GradeType.valueOf(row.getString("gradetype")),
					row.getString("justification"), row.getList("attachmentuuids", UUID.class),
					Period.ofDays(row.getInt("missed")), row.getUuid("id"));
			tickets.add(t);
		});
		Ticket target = tickets.stream().filter(p -> p.getId().equals(UUID.fromString(id))).findFirst().orElse(null);
		if (target != null) {
			target.setStatus(TicketStatus.HEAD);
			createHead(target, target.getDept());

			String q3 = "Delete from supertickets where supervisor=? and date=?";
			SimpleStatement s3 = new SimpleStatementBuilder(q3)
					.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
			BoundStatement b3 = session.prepare(s3).bind(supervisor, target.getDate());
			session.execute(b3);
			return target;

		}
		return null;
	}

	public Ticket approveHeadTicket(Department dept, String id) {
		String q1 = "Select * from headtickets where dept=?";

		SimpleStatement s1 = new SimpleStatementBuilder(q1).build();
		BoundStatement b1 = session.prepare(s1).bind(dept.name());
		List<Ticket> tickets = new ArrayList<Ticket>();
		ResultSet rs = session.execute(b1);
		rs.forEach(row -> {
			Ticket t = new Ticket(row.getString("username"),
					new BasicInfo(row.getString("firstname"), row.getString("lastname"), row.getString("email")),
					row.getLocalDate("date"), row.getString("location"), Department.valueOf(row.getString("dept")),
					row.getDouble("cost"), GradeType.valueOf(row.getString("gradetype")),
					row.getString("justification"), row.getList("attachmentuuids", UUID.class),
					Period.ofDays(row.getInt("missed")), row.getUuid("id"));
			tickets.add(t);
		});
		Ticket target = tickets.stream().filter(p -> p.getId().equals(UUID.fromString(id))).findFirst().orElse(null);
		if (target != null) {
			target.setStatus(TicketStatus.BENCO);
			String q2 = "Insert into bencotickets (username, email, firstname, lastname, date, location, cost, gradetype, justification, attachmentuuids, missed, ticketstatus, id, dept) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

			SimpleStatement s2 = new SimpleStatementBuilder(q2)
					.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();

			BoundStatement b2 = session.prepare(s2).bind(target.getUser(), target.getInfo().getEmail(),
					target.getInfo().getFirstName(), target.getInfo().getLastName(), target.getDate(),
					target.getLocation(), target.getCost(), target.getGradeType().name(), target.getJustification(),
					target.getAttachments(), target.getMissedTime().getDays(), target.getStatus().name(),
					target.getId(), target.getDept().name());
			session.execute(b2);

			String q3 = "Delete from headtickets where dept=? and date=?";
			SimpleStatement s3 = new SimpleStatementBuilder(q3)
					.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
			BoundStatement b3 = session.prepare(s3).bind(dept.name(), target.getDate());
			session.execute(b3);
			return target;
		}
		return null;
	}

	public Ticket approveBenCoTicket(String id) {

		String q1 = "Select * from bencotickets where id=?";

		SimpleStatement s1 = new SimpleStatementBuilder(q1).build();
		BoundStatement b1 = session.prepare(s1).bind(UUID.fromString(id));
		List<Ticket> tickets = new ArrayList<Ticket>();
		ResultSet rs = session.execute(b1);
		rs.forEach(row -> {
			Ticket t = new Ticket(row.getString("username"),
					new BasicInfo(row.getString("firstname"), row.getString("lastname"), row.getString("email")),
					row.getLocalDate("date"), row.getString("location"), Department.valueOf(row.getString("dept")),
					row.getDouble("cost"), GradeType.valueOf(row.getString("gradetype")),
					row.getString("justification"), row.getList("attachmentuuids", UUID.class),
					Period.ofDays(row.getInt("missed")), row.getUuid("id"));
			tickets.add(t);
		});
		Ticket target = tickets.stream().filter(p -> p.getId().equals(UUID.fromString(id))).findFirst().orElse(null);
		if (target != null) {
			target.setStatus(TicketStatus.PENDING_GRADE);
			String q2 = "Update tickets set ticketstatus=? where username=? and id =?;";

			SimpleStatement s2 = new SimpleStatementBuilder(q2)
					.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();

			BoundStatement b2 = session.prepare(s2).bind(target.getStatus().name(), target.getUser(), target.getId());
			session.execute(b2);

			String q4 = "Select pendingbalance from users where username=?";

			SimpleStatement s4 = new SimpleStatementBuilder(q4)
					.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
			BoundStatement b4 = session.prepare(s4).bind(target.getUser());

			ResultSet set = session.execute(b4);
			Double x = set.one().getDouble("pendingbalance");
			Double ans = x + target.getCost();

			String q5 = "Update users set pendingbalance=? where username=?";
			SimpleStatement s5 = new SimpleStatementBuilder(q5)
					.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
			BoundStatement b5 = session.prepare(s5).bind(ans, target.getUser());
			session.execute(b5);

			String q3 = "Update bencotickets set ticketstatus=? where id=?";
			SimpleStatement s3 = new SimpleStatementBuilder(q3)
					.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
			BoundStatement b3 = session.prepare(s3).bind(target.getStatus().name(), target.getId());
			session.execute(b3);
			return target;
		}
		return null;
	}

	public InputStream getTicketFile(String id) {
		return S3Util.getInstance().getObject(id);
	}

	public Ticket insertGrade(String fileid, String username, String ticketid) {
		String q1 = "Select * from tickets where username=? and id=?";

		SimpleStatement s1 = new SimpleStatementBuilder(q1).build();
		BoundStatement b1 = session.prepare(s1).bind(username, UUID.fromString(ticketid));
		List<Ticket> tickets = new ArrayList<Ticket>();
		ResultSet rs = session.execute(b1);
		Row row = rs.one();
			Ticket t = new Ticket(row.getString("username"),
					new BasicInfo(row.getString("firstname"), row.getString("lastname"), row.getString("email")),
					row.getLocalDate("date"), row.getString("location"), Department.valueOf(row.getString("dept")),
					row.getDouble("cost"), GradeType.valueOf(row.getString("gradetype")),
					row.getString("justification"), row.getList("attachmentuuids", UUID.class),
					Period.ofDays(row.getInt("missed")), row.getUuid("id"));
		t.getAttachments().add(UUID.fromString(fileid));
		String q2 = "Update tickets set attachmentsuuid=? ticketstatus=? where username=? and id=?";
		SimpleStatement s2 = new SimpleStatementBuilder(q2).build();
		BoundStatement b2 = session.prepare(s2).bind(t.getAttachments(), TicketStatus.PENDING_COMPLETION.name(),
				username, t.getId());
		session.execute(b2);

		String q3 = "Insert into bencotickets (username, email, firstname, lastname, date, location, cost, gradetype, justification, attachmentuuids, missed, ticketstatus, id, dept) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

		SimpleStatement s3 = new SimpleStatementBuilder(q3).setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM)
				.build();

		BoundStatement b3 = session.prepare(s3).bind(t.getUser(), t.getInfo().getEmail(), t.getInfo().getFirstName(),
				t.getInfo().getLastName(), t.getDate(), t.getLocation(), t.getCost(), t.getGradeType().name(),
				t.getJustification(), t.getAttachments(), t.getMissedTime().getDays(), t.getStatus().name(), t.getId(),
				t.getDept().name());
		session.execute(b3);

		return t;
	}

	public Ticket approveGrade(String id, double percentage) {
		String q1 = "Select * from bencotickets where id=?";

		SimpleStatement s1 = new SimpleStatementBuilder(q1).build();
		BoundStatement b1 = session.prepare(s1).bind(UUID.fromString(id));
		ResultSet rs = session.execute(b1);
		Row row =rs.one();
		if(row == null) {
			return null;
		}
		Ticket target = new Ticket(row.getString("username"),
					new BasicInfo(row.getString("firstname"), row.getString("lastname"), row.getString("email")),
					row.getLocalDate("date"), row.getString("location"), Department.valueOf(row.getString("dept")),
					row.getDouble("cost"), GradeType.valueOf(row.getString("gradetype")),
					row.getString("justification"), row.getList("attachmentuuids", UUID.class),
					Period.ofDays(row.getInt("missed")), row.getUuid("id"));
		
		if (target != null) {
			target.setStatus(TicketStatus.FIN);
			String q2 = "Update tickets set ticketstatus=? where username=? and id =?";

			SimpleStatement s2 = new SimpleStatementBuilder(q2)
					.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();

			BoundStatement b2 = session.prepare(s2).bind(target.getStatus().name(), target.getUser(), target.getId());
			session.execute(b2);

			String q4 = "Select pendingbalance from users where username=?";

			SimpleStatement s4 = new SimpleStatementBuilder(q4)
					.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
			BoundStatement b4 = session.prepare(s4).bind(target.getUser());

			ResultSet set = session.execute(b4);
			Double x = set.one().getDouble("pendingbalance");
			Double ans = target.getCost()-x;

			String q5 = "Update users set awardedbalance=?, pendingbalance=? where username=?";
			SimpleStatement s5 = new SimpleStatementBuilder(q5)
					.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
			BoundStatement b5 = session.prepare(s5).bind(x*percentage, ans , target.getUser());
			session.execute(b5);

			String q3 = "Delete from bencotickets where id=?";
			SimpleStatement s3 = new SimpleStatementBuilder(q3)
					.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
			BoundStatement b3 = session.prepare(s3).bind(target.getId());
			session.execute(b3);
		}
		return target;
	}

	public Ticket disapproveSuperTicket(String supervisor, String id) {
		String q1 = "Select * from supertickets where supervisor=?";

		SimpleStatement s1 = new SimpleStatementBuilder(q1).build();
		BoundStatement b1 = session.prepare(s1).bind(supervisor);
		List<Ticket> tickets = new ArrayList<Ticket>();
		ResultSet rs = session.execute(b1);
		rs.forEach(row -> {
			Ticket t = new Ticket(row.getString("username"),
					new BasicInfo(row.getString("firstname"), row.getString("lastname"), row.getString("email")),
					row.getLocalDate("date"), row.getString("location"), Department.valueOf(row.getString("dept")),
					row.getDouble("cost"), GradeType.valueOf(row.getString("gradetype")),
					row.getString("justification"), row.getList("attachmentuuids", UUID.class),
					Period.ofDays(row.getInt("missed")), row.getUuid("id"));
			tickets.add(t);
		});
		Ticket target = tickets.stream().filter(p -> p.getId().equals(UUID.fromString(id))).findFirst().orElse(null);
		if (target != null) {
			target.setStatus(TicketStatus.DISAPPROVE);
			
			String q2 = "Update tickets set ticketstatus=? where username=? id=?";
			SimpleStatement s2 = new SimpleStatementBuilder(q2).setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
			BoundStatement b2 = session.prepare(s2).bind(target.getStatus().name(),target.getUser(), target.getId());
			session.execute(b2);

			String q3 = "Delete from supertickets where supervisor=? and date=?";
			SimpleStatement s3 = new SimpleStatementBuilder(q3)
					.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
			BoundStatement b3 = session.prepare(s3).bind(supervisor, target.getDate());
			session.execute(b3);
			return target;

		}
		return null;
	}
	
	public Ticket disapproveHeadTicket(Department dept, String id) {
		String q1 = "Select * from headtickets where dept=?";

		SimpleStatement s1 = new SimpleStatementBuilder(q1).build();
		BoundStatement b1 = session.prepare(s1).bind(dept.name());
		List<Ticket> tickets = new ArrayList<Ticket>();
		ResultSet rs = session.execute(b1);
		rs.forEach(row -> {
			Ticket t = new Ticket(row.getString("username"),
					new BasicInfo(row.getString("firstname"), row.getString("lastname"), row.getString("email")),
					row.getLocalDate("date"), row.getString("location"), Department.valueOf(row.getString("dept")),
					row.getDouble("cost"), GradeType.valueOf(row.getString("gradetype")),
					row.getString("justification"), row.getList("attachmentuuids", UUID.class),
					Period.ofDays(row.getInt("missed")), row.getUuid("id"));
			tickets.add(t);
		});
		Ticket target = tickets.stream().filter(p -> p.getId().equals(UUID.fromString(id))).findFirst().orElse(null);
		if (target != null) {
			target.setStatus(TicketStatus.DISAPPROVE);
			String q2 = "Update tickets set ticketstatus=? where username=? and id=?";

			SimpleStatement s2 = new SimpleStatementBuilder(q2)
					.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();

			BoundStatement b2 = session.prepare(s2).bind(target.getStatus().name(), target.getUser(),target.getId());
			session.execute(b2);

			String q3 = "Delete from headtickets where dept=? and date=?";
			SimpleStatement s3 = new SimpleStatementBuilder(q3)
					.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
			BoundStatement b3 = session.prepare(s3).bind(dept.name(), target.getDate());
			session.execute(b3);
			return target;
		}
		return null;
	}

	public Ticket disapproveBenCoTicket(String id) {
		
		String q1 = "Select * from bencotickets where id=?";

		SimpleStatement s1 = new SimpleStatementBuilder(q1).build();
		BoundStatement b1 = session.prepare(s1).bind(UUID.fromString(id));
		List<Ticket> tickets = new ArrayList<Ticket>();
		ResultSet rs = session.execute(b1);
		rs.forEach(row -> {
			Ticket t = new Ticket(row.getString("username"),
					new BasicInfo(row.getString("firstname"), row.getString("lastname"), row.getString("email")),
					row.getLocalDate("date"), row.getString("location"), Department.valueOf(row.getString("dept")),
					row.getDouble("cost"), GradeType.valueOf(row.getString("gradetype")),
					row.getString("justification"), row.getList("attachmentuuids", UUID.class),
					Period.ofDays(row.getInt("missed")), row.getUuid("id"));
			tickets.add(t);
		});
		Ticket target = tickets.stream().filter(p -> p.getId().equals(UUID.fromString(id))).findFirst().orElse(null);
		if (target != null) {
			target.setStatus(TicketStatus.DISAPPROVE);
			String q2 = "Update tickets set ticketstatus=? where username=? and id =?;";

			SimpleStatement s2 = new SimpleStatementBuilder(q2)
					.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();

			BoundStatement b2 = session.prepare(s2).bind(target.getStatus().name(), target.getUser(), target.getId());
			session.execute(b2);
			
			return target;
		}
		return null;
	}
	
}
