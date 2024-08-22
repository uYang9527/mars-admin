package com.mars.easy.admin.modules.system.service.impl;

import com.mars.easy.admin.framework.enums.CommonResponseEnum;
import com.mars.easy.admin.framework.event.EventPublisher;
import com.mars.easy.admin.framework.event.PermissionChangeEvent;
import com.mars.easy.admin.framework.event.PermissionMeta;
import com.mars.easy.admin.framework.util.BeanCopyUtils;
import com.mars.easy.admin.framework.util.PageUtils;
import com.mars.easy.admin.framework.util.Utils;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.mars.easy.admin.modules.system.mapper.SysDataRoleMapper;
import com.mars.easy.admin.modules.system.pojo.dto.sysdatarole.SysDataRoleCreateDTO;
import com.mars.easy.admin.modules.system.pojo.dto.sysdatarole.SysDataRoleListDTO;
import com.mars.easy.admin.modules.system.pojo.dto.sysdatarole.SysDataRoleUpdateDTO;
import com.mars.easy.admin.modules.system.pojo.po.SysDataRole;
import com.mars.easy.admin.modules.system.pojo.po.SysUserDataRole;
import com.mars.easy.admin.modules.system.pojo.vo.sysdatarole.SysDataRoleMenuVO;
import com.mars.easy.admin.modules.system.pojo.vo.sysdatarole.SysDataRoleVO;
import com.mars.easy.admin.modules.system.pojo.vo.sysdept.DeptTreeVO;
import com.mars.easy.admin.modules.system.pojo.vo.sysmenu.MenuTreeVO;
import com.mars.easy.admin.modules.system.pojo.vo.sysuser.UserOptionVO;
import com.mars.easy.admin.modules.system.service.*;
import com.mars.easy.admin.framework.entity.PageResult;
import com.mars.easy.admin.framework.entity.SelectIdsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.mars.easy.admin.modules.system.pojo.po.table.SysUserDataRoleTableDef.SYS_USER_DATA_ROLE;


/**
 * <p>
 * 数据权限管理 服务实现类
 * </p>
 *
 * @Author 程序员Mars-admin
 * @since 2024-07-09
 */
@Service
@RequiredArgsConstructor
public class SysDataRoleServiceImpl extends ServiceImpl<SysDataRoleMapper, SysDataRole> implements SysDataRoleService {

    private final SysMenuService sysMenuService;

    private final SysDeptServiceImpl sysDeptService;

    private final SysUserService sysUserService;

    private final SysDataRoleMenuService sysDataRoleMenuService;

    private final SysDataRoleRelationService sysDataRoleRelationService;

    private final EventPublisher eventPublisher;

    @Transactional
    @Override
    public void create(SysDataRoleCreateDTO dto) {
        SysDataRole sysDataRole = BeanCopyUtils.copy(dto, SysDataRole.class);
        save(sysDataRole);
        sysDataRoleMenuService.batchSave(sysDataRole.getId(), dto.getSelectMenuIds());
        if (Utils.isNotNull(dto.getSelectDeptIds()))
            sysDataRoleRelationService.batchSave(sysDataRole.getId(), "1007001", dto.getSelectDeptIds());
        if (Utils.isNotNull(dto.getUserOptions()))
            sysDataRoleRelationService.batchSave(sysDataRole.getId(), "1007002", dto.getUserOptions());


    }

    @Transactional
    @Override
    public void update(SysDataRoleUpdateDTO dto) {
        SysDataRole sysDataRole = BeanCopyUtils.copy(dto, SysDataRole.class);
        QueryWrapper wrapper;
        // id有效性校验
        wrapper = QueryWrapper.create()
                .eq(SysDataRole::getId, dto.getId());
        CommonResponseEnum.INVALID_ID.assertTrue(count(wrapper) <= 0);
        sysDataRoleMenuService.batchSave(sysDataRole.getId(), dto.getSelectMenuIds());
        sysDataRoleRelationService.batchSave(sysDataRole.getId(), "1007001", dto.getSelectDeptIds());
        sysDataRoleRelationService.batchSave(sysDataRole.getId(), "1007002", dto.getUserOptions());
        saveOrUpdate(sysDataRole);

        List<Long> changeUserIds = QueryChain.of(SysUserDataRole.class)  // 查询用户影响范围
                .select(SYS_USER_DATA_ROLE.USER_ID)
                .where(SYS_USER_DATA_ROLE.ROLE_ID.eq(dto.getId())).listAs(Long.class);
        eventPublisher.publish(new PermissionChangeEvent(this, new PermissionMeta(changeUserIds)));
    }

    @Override
    public PageResult<SysDataRoleVO> page(SysDataRoleListDTO dto) {
        Page<SysDataRoleVO> page = pageAs(PageUtils.getPage(dto), buildQueryWrapper(dto), SysDataRoleVO.class);
        return PageUtils.getPageResult(page);
    }

    @Override
    public List<SysDataRoleVO> list(SysDataRoleListDTO dto) {
        return listAs(buildQueryWrapper(dto), SysDataRoleVO.class);
    }

    @Override
    public void remove(SelectIdsDTO dto) {
        CommonResponseEnum.INVALID_ID.assertTrue(dto.getIds().isEmpty());
        removeByIds(dto.getIds());
    }

    @Override
    public SysDataRoleVO detail(Object id) {
        Long longVal = Utils.getLongVal(id);
        SysDataRole sysDataRole = getById(longVal);
        CommonResponseEnum.INVALID_ID.assertNull(sysDataRole);
        List<String> menuIds = sysDataRoleMenuService.getSelectMenuIdByRoleId(Utils.getLongVal(id));
        List<Long> deptIds = sysDataRoleRelationService.getSelectRelationId(Utils.getLongVal(id), "1007001");
        List<Long> userOptions = sysDataRoleRelationService.getSelectRelationId(Utils.getLongVal(id), "1007002");
        SysDataRoleVO vo = BeanCopyUtils.copy(sysDataRole, SysDataRoleVO.class);
        vo.setSelectMenuIds(menuIds);
        vo.setSelectDeptIds(deptIds);
        vo.setUserOptions(userOptions);
        return vo;
    }

    private static QueryWrapper buildQueryWrapper(SysDataRoleListDTO dto) {
        QueryWrapper wrapper = QueryWrapper.create().from(SysDataRole.class);
        wrapper.like(SysDataRole::getRoleName, dto.getRoleName());
        wrapper.eq(SysDataRole::getIsLock, dto.getIsLock());
        return wrapper;
    }

    @Override
    public SysDataRoleMenuVO queryDataRoleMenu() {
        SysDataRoleMenuVO menuVO = new SysDataRoleMenuVO();
        List<MenuTreeVO> menuTreeVOS = sysMenuService.queryDataRoleMenu();
        List<DeptTreeVO> deptTreeVOS = sysDeptService.getDeptTree(null, false, false);
        List<UserOptionVO> userOptions = sysUserService.getUserOptions();
        menuVO.setMenuLists(menuTreeVOS);
        menuVO.setDeptLists(deptTreeVOS);
        menuVO.setUserOptions(userOptions);
        return menuVO;
    }


}
