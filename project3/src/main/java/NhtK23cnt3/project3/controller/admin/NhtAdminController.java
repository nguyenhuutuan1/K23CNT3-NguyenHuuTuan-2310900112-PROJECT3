package NhtK23cnt3.project3.controller.admin;

import NhtK23cnt3.project3.entity.order.NhtOrder;
import NhtK23cnt3.project3.entity.order.NhtOrderStatus;
import NhtK23cnt3.project3.entity.user.NhtUser;
import NhtK23cnt3.project3.entity.user.NhtUserRole;
import NhtK23cnt3.project3.repository.order.NhtOrderRepository;
import NhtK23cnt3.project3.repository.user.NhtUserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class NhtAdminController {

    private final NhtOrderRepository orderRepository;
    private final NhtUserRepository userRepository;

    /* ==== CHECK ADMIN ==== */
    private boolean isAdmin(NhtUser user) {
        return user != null && user.getRole() == NhtUserRole.ADMIN;
    }

    /* ==== LABEL TRẠNG THÁI ĐƠN ==== */
    private Map<String, String> buildStatusLabels() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put(NhtOrderStatus.WAIT_CONFIRM.name(), "Chờ xác nhận");
        map.put(NhtOrderStatus.PREPARING.name(),   "Đang chuẩn bị hàng");
        map.put(NhtOrderStatus.SHIPPING.name(),    "Đang giao");
        map.put(NhtOrderStatus.COMPLETED.name(),   "Đã giao thành công");
        map.put(NhtOrderStatus.CANCELLED.name(),   "Đã hủy");
        return map;
    }

    /** Redirect về đúng list theo role */
    private String redirectByRole(NhtUserRole role) {
        if (role == NhtUserRole.ADMIN) {
            return "redirect:/admin/admin-users";
        }
        return "redirect:/admin/customers";
    }

    /* ========== DASHBOARD ========== */
    @GetMapping("/dashboard")
    public String adminHome(HttpSession session, Model model) {
        NhtUser currentUser = (NhtUser) session.getAttribute("currentUser");
        if (!isAdmin(currentUser)) {
            return "redirect:/login";
        }
        model.addAttribute("adminName", currentUser.getFullName());
        return "admin/NhtAdminDashboard";
    }

    /* ========== QUẢN LÝ ĐƠN HÀNG ========== */

    @GetMapping("/orders")
    public String adminOrders(HttpSession session, Model model) {
        NhtUser currentUser = (NhtUser) session.getAttribute("currentUser");
        if (!isAdmin(currentUser)) {
            return "redirect:/login";
        }

        List<NhtOrder> orders = orderRepository.findAll();
        model.addAttribute("orders", orders);
        model.addAttribute("statusLabels", buildStatusLabels());

        return "admin/NhtAdminOrders";
    }

    @GetMapping("/orders/edit/{id}")
    public String editOrder(@PathVariable Long id,
                            HttpSession session,
                            Model model) {
        NhtUser currentUser = (NhtUser) session.getAttribute("currentUser");
        if (!isAdmin(currentUser)) {
            return "redirect:/login";
        }

        NhtOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        model.addAttribute("order", order);
        model.addAttribute("statusLabels", buildStatusLabels());
        return "admin/NhtAdminOrderEdit";
    }

    @PostMapping("/orders/update")
    public String updateOrder(@RequestParam Long id,
                              @RequestParam NhtOrderStatus status,
                              HttpSession session) {
        NhtUser currentUser = (NhtUser) session.getAttribute("currentUser");
        if (!isAdmin(currentUser)) {
            return "redirect:/login";
        }

        NhtOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        order.setStatus(status);
        orderRepository.save(order);

        return "redirect:/admin/orders?updated";
    }

    @GetMapping("/orders/delete/{id}")
    public String deleteOrder(@PathVariable Long id,
                              HttpSession session) {
        NhtUser currentUser = (NhtUser) session.getAttribute("currentUser");
        if (!isAdmin(currentUser)) {
            return "redirect:/login";
        }

        orderRepository.deleteById(id);
        return "redirect:/admin/orders?deleted";
    }

    /* ========== QUẢN LÝ ADMIN (ROLE = ADMIN) ========== */

    @GetMapping("/admin-users")
    public String listAdminUsers(HttpSession session, Model model) {
        NhtUser currentUser = (NhtUser) session.getAttribute("currentUser");
        if (!isAdmin(currentUser)) {
            return "redirect:/login";
        }

        List<NhtUser> admins = userRepository.findByRole(NhtUserRole.ADMIN);
        model.addAttribute("admins", admins);
        return "admin/NhtAdminUserList";
    }

    /* ========== QUẢN LÝ KHÁCH HÀNG (ROLE = CUSTOMER) ========== */

    @GetMapping("/customers")
    public String listCustomers(HttpSession session, Model model) {
        NhtUser currentUser = (NhtUser) session.getAttribute("currentUser");
        if (!isAdmin(currentUser)) {
            return "redirect:/login";
        }

        List<NhtUser> customers = userRepository.findByRole(NhtUserRole.CUSTOMER);
        model.addAttribute("customers", customers);
        return "admin/NhtCustomerList";
    }

    /* ========== FORM THÊM USER (ADMIN / CUSTOMER) ========== */

    @GetMapping("/users/create")
    public String showCreateUserForm(@RequestParam("role") NhtUserRole role,
                                     HttpSession session,
                                     Model model) {
        NhtUser currentUser = (NhtUser) session.getAttribute("currentUser");
        if (!isAdmin(currentUser)) {
            return "redirect:/login";
        }

        NhtUser user = new NhtUser();
        user.setRole(role);

        model.addAttribute("user", user);
        model.addAttribute("formMode", "CREATE");
        return "admin/NhtUserForm";
    }

    @PostMapping("/users/create")
    public String createUser(@RequestParam String fullName,
                             @RequestParam String email,
                             @RequestParam String password,
                             @RequestParam(required = false) String phone,
                             @RequestParam NhtUserRole role,
                             HttpSession session,
                             Model model) {

        NhtUser currentUser = (NhtUser) session.getAttribute("currentUser");
        if (!isAdmin(currentUser)) {
            return "redirect:/login";
        }

        // Check trùng email
        Optional<NhtUser> opt = userRepository.findByEmail(email);
        if (opt.isPresent()) {
            model.addAttribute("error", "Email đã tồn tại");
            NhtUser u = new NhtUser();
            u.setFullName(fullName);
            u.setEmail(email);
            u.setPhone(phone);
            u.setRole(role);

            model.addAttribute("user", u);
            model.addAttribute("formMode", "CREATE");
            return "admin/NhtUserForm";
        }

        NhtUser user = new NhtUser();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(password);   // demo: chưa mã hóa
        user.setPhone(phone);
        user.setRole(role);

        userRepository.save(user);

        return redirectByRole(role);
    }

    /* ========== SỬA USER ========== */

    @GetMapping("/users/edit/{id}")
    public String showEditUserForm(@PathVariable Long id,
                                   HttpSession session,
                                   Model model) {
        NhtUser currentUser = (NhtUser) session.getAttribute("currentUser");
        if (!isAdmin(currentUser)) {
            return "redirect:/login";
        }

        NhtUser user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        model.addAttribute("user", user);
        model.addAttribute("formMode", "EDIT");
        return "admin/NhtUserForm";
    }

    @PostMapping("/users/update")
    public String updateUser(@RequestParam Long id,
                             @RequestParam String fullName,
                             @RequestParam String email,
                             @RequestParam(required = false) String password,
                             @RequestParam(required = false) String phone,
                             @RequestParam NhtUserRole role,
                             HttpSession session,
                             Model model) {

        NhtUser currentUser = (NhtUser) session.getAttribute("currentUser");
        if (!isAdmin(currentUser)) {
            return "redirect:/login";
        }

        NhtUser user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Check email trùng với người khác
        Optional<NhtUser> opt = userRepository.findByEmail(email);
        if (opt.isPresent() && !opt.get().getId().equals(id)) {
            model.addAttribute("error", "Email đã được sử dụng bởi tài khoản khác");
            user.setFullName(fullName);
            user.setEmail(email);
            user.setPhone(phone);
            user.setRole(role);

            model.addAttribute("user", user);
            model.addAttribute("formMode", "EDIT");
            return "admin/NhtUserForm";
        }

        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setRole(role);

        if (password != null && !password.isBlank()) {
            user.setPassword(password);
        }

        userRepository.save(user);

        return redirectByRole(user.getRole());
    }

    /* ========== XOÁ USER ========== */

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id,
                             HttpSession session) {
        NhtUser currentUser = (NhtUser) session.getAttribute("currentUser");
        if (!isAdmin(currentUser)) {
            return "redirect:/login";
        }

        NhtUser user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // optional: không cho tự xoá chính mình
        if (currentUser.getId().equals(user.getId())) {
            return redirectByRole(user.getRole()) + "?error=cannot_delete_yourself";
        }

        NhtUserRole role = user.getRole();
        userRepository.delete(user);

        return redirectByRole(role) + "?deleted";
    }
}
