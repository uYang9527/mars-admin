package com.mars.workflow.mapper;

import com.mars.workflow.vo.WorkflowInstanceVo;
import com.mars.workflow.vo.WorkflowTaskVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工作流自定义查询
 */
@Mapper
public interface WorkflowMapper {

    List<WorkflowTaskVo> selectTodoPage(@Param("permissions") List<String> permissions,
                                          @Param("flowName") String flowName,
                                          @Param("offset") int offset,
                                          @Param("pageSize") int pageSize);

    long countTodo(@Param("permissions") List<String> permissions,
                   @Param("flowName") String flowName);

    List<WorkflowTaskVo> selectDonePage(@Param("approver") String approver,
                                        @Param("flowName") String flowName,
                                        @Param("offset") int offset,
                                        @Param("pageSize") int pageSize);

    long countDone(@Param("approver") String approver,
                   @Param("flowName") String flowName);

    List<WorkflowInstanceVo> selectInstancePage(@Param("flowName") String flowName,
                                                @Param("flowStatus") String flowStatus,
                                                @Param("offset") int offset,
                                                @Param("pageSize") int pageSize);

    long countInstance(@Param("flowName") String flowName,
                       @Param("flowStatus") String flowStatus);
}
