package kr.adapterz.springboot.service;

import kr.adapterz.springboot.dto.Text;

import java.util.ArrayList;
import java.util.List;

public class TermService {

    private final Text term = new Text("사용자 이용약관", "이건 이용약관인데, 우리 서비스 이용하려면 지금부터 내가 하는 말들 이용약관 지켜야해");
    private final Text privacyTerm = new Text("개인정보처리방침", "이건 개인정보처리방침인데, 너가 입력한 개인정보를 우리 사이트에서만 사용할거야, 탈퇴하면 바로 없어질거야");

    public TermService() {
    }

    public Text getTerm() {
        return term;
    }

    public Text getPrivacyTerm() {
        return privacyTerm;
    }
}
