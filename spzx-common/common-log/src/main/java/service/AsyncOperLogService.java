package service;

import com.atguigu.spzx.model.entity.system.SysOperLog;

public interface AsyncOperLogService {
    void saveSysOperLog(SysOperLog sysOperLog);
}
