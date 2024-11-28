package com.example.demo.controller.user;

import com.example.demo.Utils.Modal;
import com.example.demo.Utils.PreferencesUtils;
import javafx.event.ActionEvent;
import java.io.IOException;

import static com.example.demo.config.button.ButtonHandler.handleNavigator;

public class SalesDashboardLayout   {
    public void onPayment(ActionEvent actionEvent) throws IOException {
        Modal.showModal("/com/example/demo/controller/auth/view/auth/paymentprocessing/paymentProcessing.fxml","Chọn các hình thức thanh toán bằng cách bấm vào ô tương ứng.");
    }
    public void onOrderList(ActionEvent actionEvent) throws  IOException {
        Modal.showModal("/com/example/demo/controller/auth/view/auth/orderlist/order-list.fxml","Danh sách đơn hàng");
    }
    public void onReport(ActionEvent actionEvent) throws IOException {
        Modal.showModal("/com/example/demo/controller/auth/view/auth/report/report.fxml", "Báo cáo nhanh");
    }
    public void onTranslation(ActionEvent actionEvent) throws IOException{
        Modal.showModal("/com/example/demo/controller/auth/view/auth/translation/translation.fxml", "Dịch vụ");
    }
    public void onMember(ActionEvent actionEvent) throws IOException{
        Modal.showModal("/com/example/demo/controller/auth/view/auth/member/member.fxml", "Thông tin khách hàng");
    }
    public void onCheckPrice(ActionEvent actionEvent) throws IOException{
        Modal.showModal("/com/example/demo/controller/auth/view/auth/checkprice/checkprice.fxml", "Kiểm tra giá sản phẩm");
    }
    public void onOut(ActionEvent actionEvent) {
        PreferencesUtils.clearAll();
        handleNavigator(actionEvent, "/com/example/demo/controller/auth/login-view.fxml", false);
    }
    public void onCloseshift(ActionEvent actionEvent) throws IOException{
        Modal.showModal("/com/example/demo/controller/auth/view/auth/closeshift/closeshift.fxml", "Kết thúc ca");

    }
}
