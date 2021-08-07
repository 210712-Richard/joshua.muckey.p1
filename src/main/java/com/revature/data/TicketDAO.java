package com.revature.data;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BatchStatement;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.DefaultBatchType;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatementBuilder;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.core.type.TupleType;
import com.revature.models.Department;
import com.revature.models.Ticket;
import com.revature.util.CassandraUtil;

public class TicketDAO {

	private CqlSession session = CassandraUtil.getInstance().getSession();
	private static final TupleType DATE_TUPLE = DataTypes.tupleOf(DataTypes.INT, DataTypes.INT, DataTypes.INT, DataTypes.INT, DataTypes.INT);
	
	public Ticket createSuper(Ticket ticket, String supervisor) {
		
		BoundStatement b1 = basicCreate(ticket);
		BoundStatement b3 = timeCreate(ticket, null, supervisor);
		
		String q2 = "Insert into supertickets (supervisor ,username, email, firstname, lastname, "+
				"date, location, cost, gradeType,  justification, attachmentuuids,"+
				" missedTime, ticketstatus) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		
		SimpleStatement s2 = new SimpleStatementBuilder(q2).build();
		
		
		
		BoundStatement b2 = session.prepare(s2).bind(supervisor ,ticket.getUser(), ticket.getInfo().getEmail(),
				ticket.getInfo().getFirstName(), ticket.getInfo().getLastName(), ticket.getDate(), ticket.getLocation(), 
				ticket.getGradeType().name(), ticket.getJustification(), ticket.getAttachmentsId(),
				ticket.getMissedTime().get(ChronoUnit.DAYS), ticket.getStatus().name());
		
		BatchStatement batch = BatchStatement.builder(DefaultBatchType.LOGGED)
				.addStatement(b1)
				.addStatement(b2)
				.addStatement(b3)
				.build();
		session.execute(batch);
		
		return ticket;
	}

	public Ticket createHead(Ticket ticket, Department dept) {
		
		BoundStatement b1 = basicCreate(ticket);
		BoundStatement b3 = timeCreate(ticket, dept, null);
		
		String q2 = "Insert into headtickets (dept ,username, email, firstname, lastname, "+
				"date, location, cost, gradeType,  justification, attachmentuuids,"+
				" missedTime, ticketstatus) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		
		SimpleStatement s2 = new SimpleStatementBuilder(q2).build();
		
		BoundStatement b2 = session.prepare(s2).bind(dept.name() ,ticket.getUser(), ticket.getInfo().getEmail(),
				ticket.getInfo().getFirstName(), ticket.getInfo().getLastName(), ticket.getDate(), ticket.getLocation(), 
				ticket.getGradeType().name(), ticket.getJustification(), ticket.getAttachmentsId(),
				ticket.getMissedTime().get(ChronoUnit.DAYS), ticket.getStatus().name());
		
		BatchStatement batch = BatchStatement.builder(DefaultBatchType.LOGGED)
				.addStatement(b1)
				.addStatement(b2)
				.addStatement(b3)
				.build();
		session.execute(batch);
		
		return ticket;
	}
	
	private BoundStatement basicCreate(Ticket ticket) {
		String q1 = "Insert into tickets (username, email, firstname, lastname, "+
				"date, location, cost, gradeType,  justification, attachmentuuids,"+
				" missedTime, ticketstatus) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		
		SimpleStatement s1 = new SimpleStatementBuilder(q1).build();
	
		BoundStatement b1 = session.prepare(s1).bind(ticket.getUser(), ticket.getInfo().getEmail(),
				ticket.getInfo().getFirstName(), ticket.getInfo().getLastName(), ticket.getDate(), ticket.getLocation(), 
				ticket.getGradeType().name(), ticket.getJustification(), ticket.getAttachmentsId(),
				ticket.getMissedTime().get(ChronoUnit.DAYS), ticket.getStatus().name());
		return b1;
	}
	
	private BoundStatement timeCreate(Ticket ticket, Department dept, String supervisor) {
		String q1 = "Insert into timedtickets (timeinsert, "+dept!=null?"dept":"supervisor"+
				", username, ticketstatus) values (?, ?, ?, ?);";
		
		SimpleStatement s1 = new SimpleStatementBuilder(q1).build();

		
		BoundStatement b1 = session.prepare(s1).bind("toTimeStamp(now())", dept!=null? dept :supervisor,
				ticket.getUser(), ticket.getStatus().name());
		return b1;
	}

}
