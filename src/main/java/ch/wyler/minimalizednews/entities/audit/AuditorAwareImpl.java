package ch.wyler.minimalizednews.entities.audit;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

	/**
	 * The user should always be "admin" - there are no other users currently
	 * @return username
	 */
	@Override
	public Optional<String> getCurrentAuditor() {
		return Optional.of("Admin");
	}
}
