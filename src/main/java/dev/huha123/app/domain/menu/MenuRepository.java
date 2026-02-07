package dev.huha123.app.domain.menu;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<MenuEntity, Long> {

    List<MenuEntity> findAllByVisibleIsTrueOrderBySortOrderAsc();
}
