package com.techelevator.dao;

import com.techelevator.model.Invitation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcInvitationDao implements InvitationDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcInvitationDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Invitation> getInvitations(int id) {
        List<Invitation> invitations = new ArrayList<>();
        String sql = "SELECT * FROM invitation "
                + "WHERE host_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,id);
        while(results.next()) {
            Invitation invitation = mapRowToInvitation(results);
            invitations.add(invitation);
        }
        return invitations;
    }

    @Override
    public Invitation getInvitation(int id) {
        Invitation invitation = new Invitation();
        String sql = "SELECT * FROM invitation "
                + "WHERE invitation_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if(results.next()) {
            invitation = mapRowToInvitation(results);
        }

        return invitation;
    }

    @Override
    public boolean createInvitation(Invitation invite) {
        String sql = "INSERT INTO invitation (host_id, city, restaurant, meeting_date, decision_date) "
                + "VALUES (?, ?, ?, ?, ?);";

        return jdbcTemplate.update(sql,invite.getHostId(),invite.getCity(),invite.getRestaurant(),invite.getMeetingDate(),invite.getDecisionDate()) == 1;
    }

    @Override
    public boolean updateInvitation(Invitation invite) {
        String sql = "UPDATE invitation SET restaurant = ? WHERE invitation_id = ?;";
        return jdbcTemplate.update(sql,invite.getRestaurant(),invite.getInvitationId()) == 1;
    }

    private Invitation mapRowToInvitation(SqlRowSet rowSet) {
        Invitation invite = new Invitation();
        invite.setInvitationId(rowSet.getInt("invitation_id"));
        invite.setHostId(rowSet.getInt("host_id"));
        invite.setCity(rowSet.getString("city"));
        invite.setRestaurant("restaurant");
        invite.setMeetingDate(rowSet.getDate("appointment"));
        invite.setDecisionDate(rowSet.getDate("decisionDate"));

        return invite;
    }
}