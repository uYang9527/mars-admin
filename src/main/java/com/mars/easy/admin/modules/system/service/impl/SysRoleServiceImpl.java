package com.mars.easy.admin.modules.system.service.impl;


import com.mars.easy.admin.framework.enums.CommonResponseEnum;
import com.mars.easy.admin.framework.util.BeanCopyUtils;
import com.mars.easy.admin.framework.util.PageUtils;
import com.mars.easy.admin.framework.util.Utils;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.mars.easy.admin.modules.system.mapper.SysRoleMapper;
import com.mars.easy.admin.modules.system.mapper.SysRoleMenuMapper;
import com.mars.easy.admin.modules.system.pojo.dto.sysrole.SysRoleCreateDTO;
import com.mars.easy.admin.modules.system.pojo.dto.sysrole.SysRoleListDTO;
import com.mars.easy.admin.modules.system.pojo.dto.sysrole.SysRoleUpdateDTO;
import com.mars.easy.admin.modules.system.pojo.po.SysRole;
import com.mars.easy.admin.modules.system.pojo.po.SysRoleMenu;
import com.mars.easy.admin.modules.system.service.SysRoleService;
import com.mars.easy.admin.framework.entity.PageResult;
import com.mars.easy.admin.framework.entity.SelectIdsDTO;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * <p>
 * 系统角色表 服务实现类
 * </p>
 *
 * @Author 程序员Mars
 * @since 2022-10-01
 */
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public void create(SysRoleCreateDTO dto) {
        SysRole sysRole = BeanCopyUtils.copy(dto, SysRole.class);
        long count;
        count = QueryChain.of(SysRole.class).eq(SysRole::getRoleName, dto.getRoleName()).count();
        CommonResponseEnum.EXISTS.message("roleName已存在").assertTrue(count > 0);
        count = QueryChain.of(SysRole.class).eq(SysRole::getPermissions, dto.getPermissions()).count();
        CommonResponseEnum.EXISTS.message("permissions已存在").assertTrue(count > 0);
        save(sysRole);
    }

    @Override
    public void update(SysRoleUpdateDTO dto) {
        SysRole sysRole = BeanCopyUtils.copy(dto, SysRole.class);
        long count;
        count = QueryChain.of(SysRole.class).eq(SysRole::getRoleName, dto.getRoleName())
                .ne(SysRole::getId, dto.getId()).count();
        CommonResponseEnum.EXISTS.message("roleName已存在").assertTrue(count > 0);
        count = QueryChain.of(SysRole.class).eq(SysRole::getPermissions, dto.getPermissions())
                .ne(SysRole::getId, dto.getId()).count();
        CommonResponseEnum.EXISTS.message("permissions已存在").assertTrue(count > 0);
        updateById(sysRole);
    }

    @Override
    public void remove(SelectIdsDTO dto) {
        removeById((Serializable) dto.getIds());
    }

    @Override
    public void removeByMenuId(SelectIdsDTO dto) {
        QueryWrapper wrapper = QueryWrapper.create()
                .in(SysRoleMenu::getMenuId, dto.getIds());
        sysRoleMenuMapper.deleteByQuery(wrapper);
    }

    @Override
    public PageResult<SysRole> list(SysRoleListDTO dto) {
        QueryWrapper wrapper = QueryWrapper.create();
        if (Utils.isNotNull(dto.getRoleName())) {
            wrapper.like(SysRole::getRoleName, dto.getRoleName());
        }
        return PageUtils.getPageResult(page(PageUtils.getPage(dto), wrapper));
    }


}
