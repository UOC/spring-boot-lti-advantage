package edu.uoc.elc.spring.lti.security;

import edu.uoc.elc.lti.tool.Tool;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;

/**
 * UserDetails from LTI context data. Includes Context information
 *
 * @author xaracil@uoc.edu
 */
public class User extends org.springframework.security.core.userdetails.User {

	@Getter
	private Tool tool;

	@Getter
	private Context context;

	public User(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}

	public User(String username, String password, Tool tool, Collection<? extends GrantedAuthority> authorities) {
		this(username, password, authorities);
		this.tool = tool;
		this.context = new Context(tool);
	}

	public String getEmail() {
		return tool.getUser().getEmail();
	}

	public String getFullName() {
		return tool.getUser().getName();
	}

	public String getLocale() { return tool.getLocale(); }

	public List<String> getRoles() {
		return tool.getRoles();
	}

	public URI getPhotoUrl() {
		try {
			return new URI(tool.getUser().getPicture());
		} catch (URISyntaxException e) {
			return null;
		}
	}

	public Object getCustomParameter(String name) {
		return tool.getCustomParameter(name);
	}
}
