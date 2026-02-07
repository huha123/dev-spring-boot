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
        if (menuDto.parentId() != null) {
            parent = menuRepository.findById(menuDto.parentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent menu not found"));
        }

        MenuEntity menu = MenuEntity.builder()
                .menuName(menuDto.menuName())
                .sortOrder(menuDto.sortOrder())
                .parent(parent)
                .role(menuDto.role())
                .pathUrl(menuDto.pathUrl())
                .visible(menuDto.visible())
                .menuType(menuDto.menuType())
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

        MenuEntity parent = null;
        if (menuDto.parentId() != null) {
            parent = menuRepository.findById(menuDto.parentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent menu not found"));
        }

        menu = menu.toBuilder()
                .menuName(menuDto.menuName())
                .sortOrder(menuDto.sortOrder())
                .role(menuDto.role())
                .pathUrl(menuDto.pathUrl())
                .visible(menuDto.visible())
                .menuType(menuDto.menuType())
                .parent(parent)
                .build();

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

        dto = dto.withChildren(children);
        return dto;
    }

    public List<MenuDto> getVisibleMenuHierarchy() {
        List<MenuEntity> allVisibleMenus = menuRepository.findAllByVisibleIsTrueOrderBySortOrderAsc();
        return allVisibleMenus.stream()
                .filter(menu -> menu.getParent() == null)
                .map(menu -> buildVisibleHierarchy(menu, allVisibleMenus))
                .collect(Collectors.toList());
    }

    private MenuDto buildVisibleHierarchy(MenuEntity menuEntity, List<MenuEntity> allVisibleMenus) {
        MenuDto dto = MenuDto.fromEntity(menuEntity);

        List<MenuDto> children = allVisibleMenus.stream()
                .filter(menu -> menu.getParent() != null && menu.getParent().getId().equals(menuEntity.getId()))
                .map(menu -> buildVisibleHierarchy(menu, allVisibleMenus))
                .collect(Collectors.toList());

        dto = dto.withChildren(children);
        return dto;
    }
}
