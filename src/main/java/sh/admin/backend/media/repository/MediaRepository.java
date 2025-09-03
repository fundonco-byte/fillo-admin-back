package sh.admin.backend.media.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sh.admin.backend.media.domain.Media;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
}
