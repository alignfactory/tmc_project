package com.tmc.client.main;

import com.tmc.client.app.sys.model.UserModel;
import com.tmc.client.service.InterfaceServiceCall;
import com.tmc.client.service.ServiceCall;
import com.tmc.client.service.ServiceRequest;
import com.tmc.client.service.ServiceResult;
import com.tmc.client.ui.SimpleMessage;
import com.tmc.client.ui.img.ResourceIcon;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FormPanel;
import com.sencha.gxt.widget.core.client.form.PasswordField;
import com.sencha.gxt.widget.core.client.form.TextField;

public class Login implements InterfaceServiceCall {
	
	private final Dialog loginDialog = new Dialog();
	private TextField firstName = new TextField();
	private PasswordField password= new PasswordField();
	
	public Login() {
	}
	
	public void open(){
		
		firstName.setText("alignfactory@gmail.com");
		password.setText("1234");
		 
		loginDialog.setBodyBorder(false);
		loginDialog.getHeader().setIcon(ResourceIcon.INSTANCE.dBButton() ); //(+) 이미지를 가져온다. ;
		loginDialog.setResizable(false);
		loginDialog.setHeading("사용자 로그인");
		loginDialog.setHeaderVisible(true);
		loginDialog.setWidth(380);
		loginDialog.setHeight(400);
		
		loginDialog.getButton(PredefinedButton.OK).setWidth(60);
		loginDialog.getButton(PredefinedButton.OK).addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				getService(); // 함수로 빼서 호출한다. 
			}
		});
		
		FormPanel panel = new FormPanel();
		panel.setHeight(260);
		
		VerticalLayoutContainer vlc = new VerticalLayoutContainer();
		panel.add(vlc, new MarginData(30));
		
		VerticalLayoutData vld = new VerticalLayoutData(); 
		
		FieldLabel firstNameLabel = new FieldLabel(firstName, "Login ID ");
		firstNameLabel.setLabelWidth(70);
		firstNameLabel.setWidth(280);
		vlc.add(firstNameLabel, vld);
		
		FieldLabel passwordLabel = new FieldLabel(password, "Password ");  
		passwordLabel.setLabelWidth(70); 
		passwordLabel.setWidth(280); 
		vlc.add(passwordLabel, vld);
  
		Label loginDesc = new HTML("<br>반갑습니다. <br>하하정보의 유치원관리 프로그램입니다.. <br>로그인 아이디는 등록된 이메일 아이디를 사용하여 주시기 바랍니다. 오류 시 담당자에게 문의하여 주시기 바랍니다.<br>");
		vlc.add(loginDesc);
		vlc.add(new HTML("<div align='center'><br><img src='img/logo.gif' width='250' height='70'></div>"));
		panel.setLayoutData(new Margins(10, 0, 0, 30));
		loginDialog.add(panel); 
		loginDialog.show();
	}

	public void getService(){
		ServiceRequest request = new ServiceRequest("sys.User.getLoginInfo");
		request.add("loginId", firstName.getText());
		request.add("passwd", password.getText());

		ServiceCall service = new ServiceCall(); 
		service.execute(request, this);
	}
	
	@Override
	public void getServiceResult(ServiceResult result) {
		if(result.getStatus() > 0){
			UserModel user = (UserModel) result.getResult(0); 
			LoginUser.setLoginUser(user); 
			
			if(LoginUser.isAdmin()) {
				// 관리자이다. 로그인할 회사를 선택한다. 
				Select_Company loginCompany = new Select_Company();
				loginCompany.show();
			}
			else {
				// 일반 사용자이다. 회사 선택없이 로드인한다. 
				new MainWindow();  
			}
			
			loginDialog.hide();
		}
		else {
			new SimpleMessage("로그인 실패", result.getMessage()); 
		}
	}
}
