package ca.sheridancollege.uppkaram.Security;


	import java.util.ArrayList;
	import java.util.List;

	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.context.annotation.Lazy;
	import org.springframework.security.core.GrantedAuthority;
	import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
	import org.springframework.security.core.userdetails.UserDetailsService;
	import org.springframework.security.core.userdetails.UsernameNotFoundException;
	import org.springframework.stereotype.Service;

import ca.sheridancollege.uppkaram.DatabaseAccess.DatabaseAcc;
@Service
public class UserDetailsServiceImp1 implements UserDetailsService
{
	@Autowired
	@Lazy
	private DatabaseAcc da;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
	{
		ca.sheridancollege.uppkaram.Beans.User user = da.findUserAccount(username);
		
		if(user == null)
		{
			System.out.println("User not found:" + username);
			throw new UsernameNotFoundException("User" + username + " user was not found in the database");
		}
		
		List<String> roleNameList = da.getRolesById(user.getUserId());
		
		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
		if (roleNameList != null)
		{
			for (String role: roleNameList)
			{
				grantList.add(new SimpleGrantedAuthority(role));
			}
		}
		
		UserDetails userDetails = (UserDetails) new org.springframework.security.core.userdetails.User
		(
		        user.getEmail(),
		        user.getEncrptedPassword(),
		        grantList
		);
		return userDetails;
	}
}
