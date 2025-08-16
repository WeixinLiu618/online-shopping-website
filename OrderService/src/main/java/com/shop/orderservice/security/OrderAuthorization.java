package com.shop.orderservice.security;

import com.shop.orderservice.repository.OrdersByIdRepository;
import com.shop.orderservice.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

@Component("orderAuthorization")
@RequiredArgsConstructor
public class OrderAuthorization {

    private final OrdersByIdRepository ordersByIdRepository;
    private final JwtUtil jwtUtil;

    private String authHeader() {
        var attrs = RequestContextHolder.getRequestAttributes();
        if (attrs instanceof ServletRequestAttributes sra) {
            return sra.getRequest().getHeader("Authorization");
        }
        return null;
    }

    private UUID currentUserIdFromToken() {
        String h = authHeader();
        if (h != null && h.startsWith("Bearer ")) {
            return jwtUtil.userId(h.substring(7));
        }
        return null;
    }

    private boolean isAdmin() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
    }

    /** 本人或管理员可读 */
    public boolean canRead(UUID orderId) {
        if (isAdmin()) return true;
        UUID uid = currentUserIdFromToken();
        if (uid == null) return false;
        return ordersByIdRepository.findById(orderId).map(o -> uid.equals(o.getUserId())).orElse(false);
    }

    /** 本人或管理员可取消 */
    public boolean canCancel(UUID orderId) { return canRead(orderId); }
}
