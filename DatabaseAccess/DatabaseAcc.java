package ca.sheridancollege.uppkaram.DatabaseAccess;


	import java.util.List;

	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.dao.EmptyResultDataAccessException;
	import org.springframework.jdbc.core.BeanPropertyRowMapper;
	import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
	import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
	import org.springframework.security.crypto.password.PasswordEncoder;
	import org.springframework.stereotype.Repository;
	import ca.sheridancollege.uppkaram.Beans.User;

	@Repository
	public class DatabaseAcc 
	{
		@Autowired
		private NamedParameterJdbcTemplate jdbc;
		
		@Autowired
		private PasswordEncoder passwordEncoder;
		
		public User findUserAccount(String email)
		{
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			
			String query = "SELECT * FROM sec_user where email= :email";
	namedParameters.addValue("email", email);
	
	try
	{
		return jdbc.queryForObject(query, namedParameters, new BeanPropertyRowMapper<User>(User.class));
	}
	catch (EmptyResultDataAccessException erdae)
	{
		return null;
	}
}

public List<String> getRolesById(Long userId)
{
	MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	
	String query = "SELECT sec_role.roleName "
			+ "FROM user_role, sec_role "
			+ "WHERE user_role.roleId = sec_role.roleId "
			+ "AND userId = :userId";
	namedParameters.addValue("userId", userId);
	
	return jdbc.queryForList(query, namedParameters, String.class);
			
}

public void addUser(String email, String password)
{
	MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	
	String query = "INSERT INTO sec_user "
			+ "(email, encrptedPassword, enabled) "
			+ "VALUES (:email, :encrptedPassword, 1)";
	namedParameters.addValue("email", email );
	namedParameters.addValue("encrptedPassword", passwordEncoder.encode(password));
	
	jdbc.update(query, namedParameters);
}

public void addRole(Long userId, Long roleId)
{
	MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	
	String query = "INSERT INTO user_role (userId, roleId) "
			+ "VALUES (:userId, :roleId)";
	namedParameters.addValue("userId", userId);
	namedParameters.addValue("roleId", roleId);
		
		jdbc.update(query, namedParameters);
	}
}