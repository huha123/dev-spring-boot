package dev.huha123.app.service;

import dev.huha123.app.dto.MenuDto;
import dev.huha123.app.entity.MenuEntity;
import dev.huha123.app.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;

    @Transactional
    public MenuDto createMenu(MenuDto menuDto) {
        MenuEntity parent = null;
        if (menuDto.getParentId() != null) {
            parent = menuRepository.findById(menuDto.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent menu not found"));
        }

        MenuEntity menu = MenuEntity.builder()
                .menuName(menuDto.getMenuName())
                .sortOrder(menuDto.getSortOrder())
                .parent(parent)
                .role(menuDto.getRole())
                .pathUrl(menuDto.getPathUrl())
                .visible(menuDto.isVisible())
                .menuType(menuDto.getMenuType())
                .build();

        return MenuDto.fromEntity(menuRepository.save(menu));
    }

    public List<MenuDto> getAllMenus() {
        return menuRepository.findAll().stream()
                .map(MenuDto::fromEntity)
                .collect(Collectors.toList());
    }

    public MenuDto getMenuById(Long id) {
        MenuEntity menu = menuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Menu not found"));
        return MenuDto.fromEntity(menu);
    }

    @Transactional
    public MenuDto updateMenu(Long id, MenuDto menuDto) {
        MenuEntity menu = menuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Menu not found"));

        if (menuDto.getMenuName() != null) {
            menu = menu.toBuilder().menuName(menuDto.getMenuName()).build();
        }
        if (menuDto.getSortOrder() != 0) {
            menu = menu.toBuilder().sortOrder(menuDto.getSortOrder()).build();
        }
        if (menuDto.getRole() != null) {
            menu = menu.toBuilder().role(menuDto.getRole()).build();
        }
        if (menuDto.getPathUrl() != null) {
            menu = menu.toBuilder().pathUrl(menuDto.getPathUrl()).build();
        }
        if (menuDto.getMenuType() != null) {
            menu = menu.toBuilder().menuType(menuDto.getMenuType()).build();
        }

        MenuEntity parent = null;
        if (menuDto.getParentId() != null) {
            parent = menuRepository.findById(menuDto.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent menu not found"));
        }
        menu = menu.toBuilder().parent(parent).build();

        return MenuDto.fromEntity(menuRepository.save(menu));
    }

    @Transactional
    public void deleteMenu(Long id) {
        menuRepository.deleteById(id);
    }

    public List<MenuDto> getMenuHierarchy() {
        return menuRepository.findAll().stream()
                .filter(menu -> menu.getParent() == null)
                .map(this::buildHierarchy)
                .collect(Collectors.toList());
    }

    private MenuDto buildHierarchy(MenuEntity menuEntity) {
        MenuDto dto = MenuDto.fromEntity(menuEntity);

        List<MenuDto> children = menuRepository.findAll().stream()
                .filter(menu -> menu.getParent() != null && menu.getParent().getId().equals(menuEntity.getId()))
                .map(this::buildHierarchy)
                .collect(Collectors.toList());

        dto.setChildren(children);
        return dto;
    }
}
