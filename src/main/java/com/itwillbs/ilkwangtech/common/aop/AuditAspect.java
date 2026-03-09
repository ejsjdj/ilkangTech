package com.itwillbs.ilkwangtech.common.aop;

import com.itwillbs.ilkwangtech.account.dto.AccountLogin;
import com.itwillbs.ilkwangtech.common.annotation.Audit;
import com.itwillbs.ilkwangtech.common.entity.AuditLog;
import com.itwillbs.ilkwangtech.common.service.AuditLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {

    private final AuditLogService auditLogService;

    @AfterReturning(pointcut = "@annotation(com.itwillbs.ilkwangtech.common.annotation.Audit)", returning = "result")
    public void logAudit(JoinPoint joinPoint, Object result) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !(auth.getPrincipal() instanceof AccountLogin user)) {
                return;
            }

            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            Audit audit = method.getAnnotation(Audit.class);

            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            String ipAddress = "unknown";
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                ipAddress = getClientIp(request);
            }

            AuditLog auditLog = AuditLog.builder()
                    .employeeNumber(user.getEmployeeNumber())
                    .memberName(user.getName())
                    .action(audit.action())
                    .entityName(audit.entity())
                    .ipAddress(ipAddress)
                    .details(getMethodDetails(joinPoint))
                    .build();

            auditLogService.save(auditLog);
            log.info("Audit Log saved: {} by {}", audit.action(), user.getEmployeeNumber());

        } catch (Exception e) {
            log.error("Failed to save audit log", e);
        }
    }

    private String getMethodDetails(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        StringBuilder sb = new StringBuilder();
        String[] paramNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        
        if (paramNames != null) {
            for (int i = 0; i < args.length; i++) {
                if (sb.length() > 0) sb.append(", ");
                String value = args[i] != null ? args[i].toString() : "null";
                
                // 비밀번호 등 민감 정보 마스킹
                if (paramNames[i].toLowerCase().contains("password") || 
                    paramNames[i].toLowerCase().contains("pwd")) {
                    value = "[PROTECTED]";
                }
                
                sb.append(paramNames[i]).append(": ").append(value);
            }
        }
        return sb.length() > 4000 ? sb.substring(0, 3997) + "..." : sb.toString();
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
