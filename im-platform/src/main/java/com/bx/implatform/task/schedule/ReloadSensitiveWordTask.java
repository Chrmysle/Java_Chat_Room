package com.bx.implatform.task.schedule;

import com.bx.implatform.util.SensitiveFilterUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReloadSensitiveWordTask {

    private final SensitiveFilterUtil sensitiveFilterUtil;

    @Scheduled(fixedRate = 60000)
    public void run() {
        log.info("【定时任务】重新装载敏感词...");
        sensitiveFilterUtil.reload();
    }
}
