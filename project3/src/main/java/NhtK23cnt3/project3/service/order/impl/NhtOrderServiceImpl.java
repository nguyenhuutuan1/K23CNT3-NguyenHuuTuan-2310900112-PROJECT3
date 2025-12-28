package NhtK23cnt3.project3.service.order.impl;

import NhtK23cnt3.project3.entity.order.NhtOrder;
import NhtK23cnt3.project3.entity.order.NhtOrderItem;
import NhtK23cnt3.project3.entity.order.NhtOrderStatus;
import NhtK23cnt3.project3.entity.user.NhtUser;
import NhtK23cnt3.project3.model.cart.NhtCartItem;
import NhtK23cnt3.project3.order.NhtCheckoutForm;
import NhtK23cnt3.project3.repository.order.NhtOrderRepository;
import NhtK23cnt3.project3.service.order.NhtOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NhtOrderServiceImpl implements NhtOrderService {

    private final NhtOrderRepository orderRepository;

    @Override
    public NhtOrder createOrderFromCart(NhtCheckoutForm form, List<NhtCartItem> cartItems, NhtUser user) {

        NhtOrder order = new NhtOrder();
        order.setCustomerName(form.getCustomerName());
        order.setPhone(form.getPhone());
        order.setAddress(form.getAddress());
        order.setNote(form.getNote());

        // ⭐ Gắn USER
        order.setUser(user);

        order.setStatus(NhtOrderStatus.WAIT_CONFIRM);
        order.setCreatedAt(LocalDateTime.now());

        double total = 0;
        List<NhtOrderItem> items = new ArrayList<>();

        for (NhtCartItem c : cartItems) {
            NhtOrderItem item = new NhtOrderItem();
            item.setOrder(order);
            item.setProductId(c.getProductId());
            item.setProductName(c.getProductName());
            item.setProductPrice(c.getPrice());
            item.setQuantity(c.getQuantity());

            total += c.getPrice() * c.getQuantity();
            items.add(item);
        }

        order.setItems(items);
        order.setTotalAmount(total);

        return orderRepository.save(order);
    }

    @Override
    public NhtOrder getById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public NhtOrder findByIdAndPhone(Long id, String phone) {
        return orderRepository.findByIdAndPhone(id, phone).orElse(null);
    }

    @Override
    public List<NhtOrder> findByPhone(String phone) {
        return orderRepository.findByPhone(phone);
    }

    @Override
    public List<NhtOrder> findByUserId(Long userId) {
        return orderRepository.findByUser_IdOrderByCreatedAtDesc(userId);
    }
}
