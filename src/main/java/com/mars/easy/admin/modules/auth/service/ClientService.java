package com.mars.easy.admin.modules.auth.service;


import com.mars.easy.admin.modules.auth.entity.ClientVO;

/**
 * @ClassName ClientService
 * @Author 程序员Mars
 * @Date 2024/2/18 8:42
 * @Version 1.0
 */
public interface ClientService {

    ClientVO getClientByClientId(Object id);

}
