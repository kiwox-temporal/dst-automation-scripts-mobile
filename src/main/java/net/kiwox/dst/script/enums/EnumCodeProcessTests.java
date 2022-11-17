package net.kiwox.dst.script.enums;

public enum EnumCodeProcessTests {

    SIGN_IN_FACEBOOK_APP(EnumAppTests.DST_ENTEL_FACEBOOK_APP, "DST-FACEBOOK-APP-SUCCES-001", "Facebook SignIn sucessfully", EnumCodeTypeTests.SUCCESS_ACTION),


    SIGN_IN_SUCCESS_ENTEL_PERU_APP(EnumAppTests.DST_ENTEL_PERU_APP, "DST-ENTEL-PERU-APP-SUCCESS-000", "Entel Peru SignIn test sucessfully", EnumCodeTypeTests.SUCCESS_ACTION),
    SIGN_IN_ERROR_ENTEL_PERU_APP(EnumAppTests.DST_ENTEL_PERU_APP, "DST-ENTEL-PERU-APP-ERROR-000", "Entel Peru SignIn failure", EnumCodeTypeTests.CATCH_ERROR),

    VERIFY_POST_PAID_BALANCE_SUCCESS_ENTEL_PERU_APP(EnumAppTests.DST_ENTEL_PERU_APP, "DST-ENTEL-PERU-APP-SUCCESS-001", "Entel Peru verify post paid balance test sucessfully", EnumCodeTypeTests.SUCCESS_ACTION),
    VERIFY_POST_PAID_BALANCE_ERROR_ENTEL_PERU_APP(EnumAppTests.DST_ENTEL_PERU_APP, "DST-ENTEL-PERU-APP-ERROR-001", "Entel Peru verify post paid balance test failure", EnumCodeTypeTests.CATCH_ERROR),

    VERIFY_POST_PAID_BAG_SUCCESS_ENTEL_PERU_APP(EnumAppTests.DST_ENTEL_PERU_APP, "DST-ENTEL-PERU-APP-SUCCESS-002", "Entel Peru verify post paid bag test sucessfully", EnumCodeTypeTests.SUCCESS_ACTION),
    VERIFY_POST_PAID_BAG_ERROR_ENTEL_PERU_APP(EnumAppTests.DST_ENTEL_PERU_APP, "DST-ENTEL-PERU-APP-ERROR-002", "Entel Peru verify post paid bag test failure", EnumCodeTypeTests.CATCH_ERROR),

    VERIFY_RECEIPT_SUCCESS_ENTEL_PERU_APP(EnumAppTests.DST_ENTEL_PERU_APP, "DST-ENTEL-PERU-APP-SUCCESS-003", "Entel Peru verify receipt test sucessfully", EnumCodeTypeTests.SUCCESS_ACTION),
    VERIFY_RECEIPT_ERROR_ENTEL_PERU_APP(EnumAppTests.DST_ENTEL_PERU_APP, "DST-ENTEL-PERU-APP-ERROR-003", "Entel Peru verify receipt test failure", EnumCodeTypeTests.CATCH_ERROR),


    VERIFY_PRE_PAID_BAG_SUCCESS_ENTEL_PERU_APP(EnumAppTests.DST_ENTEL_PERU_APP, "DST-ENTEL-PERU-APP-SUCCESS-004", "Entel Peru verify pre paid bag test sucessfully", EnumCodeTypeTests.SUCCESS_ACTION),
    VERIFY_PRE_PAID_BAG_ERROR_ENTEL_PERU_APP(EnumAppTests.DST_ENTEL_PERU_APP, "DST-ENTEL-PERU-APP-ERROR-004", "Entel Peru verify pre paid bag test failure", EnumCodeTypeTests.CATCH_ERROR),
    VERIFY_PRE_PAID_RECHARGE_SUCCESS_ENTEL_PERU_APP(EnumAppTests.DST_ENTEL_PERU_APP, "DST-ENTEL-PERU-APP-SUCCESS-005", "Entel Peru verify pre paid recharge test sucessfully", EnumCodeTypeTests.SUCCESS_ACTION),
    VERIFY_PRE_PAID_RECHARGE_ERROR_ENTEL_PERU_APP(EnumAppTests.DST_ENTEL_PERU_APP, "DST-ENTEL-PERU-APP-ERROR-005", "Entel Peru verify pre paid recharge test failure", EnumCodeTypeTests.CATCH_ERROR),


    VERIFY_POST_PAID_BENEFIT_SUCCESS_ENTEL_PERU_APP(EnumAppTests.DST_ENTEL_PERU_APP, "DST-ENTEL-PERU-APP-SUCCESS-006", "Entel Peru verify post paid benefit test sucessfully", EnumCodeTypeTests.SUCCESS_ACTION),
    VERIFY_POST_PAID_BENEFIT_ERROR_ENTEL_PERU_APP(EnumAppTests.DST_ENTEL_PERU_APP, "DST-ENTEL-PERU-APP-ERROR-006", "Entel Peru verify post paid benefit test failure", EnumCodeTypeTests.CATCH_ERROR),
    VERIFY_POST_PAID_ADDITIONAL_LINE_SUCCESS_ENTEL_PERU_APP(EnumAppTests.DST_ENTEL_PERU_APP, "DST-ENTEL-PERU-APP-SUCCESS-007", "Entel Peru verify post paid additional line test sucessfully", EnumCodeTypeTests.SUCCESS_ACTION),
    VERIFY_POST_PAID_ADDITIONAL_LINE_ERROR_ENTEL_PERU_APP(EnumAppTests.DST_ENTEL_PERU_APP, "DST-ENTEL-PERU-APP-ERROR-007", "Entel Peru verify post paid additional line test failure", EnumCodeTypeTests.CATCH_ERROR),
    VERIFY_UPSELLING_SUCCESS_ENTEL_PERU_APP(EnumAppTests.DST_ENTEL_PERU_APP, "DST-ENTEL-PERU-APP-SUCCESS-008", "Entel Peru verify upselling test sucessfully", EnumCodeTypeTests.SUCCESS_ACTION),
    VERIFY_UPSELLING_ERROR_ENTEL_PERU_APP(EnumAppTests.DST_ENTEL_PERU_APP, "DST-ENTEL-PERU-APP-ERROR-008", "Entel Peru verify upselling test failure", EnumCodeTypeTests.CATCH_ERROR),
    VERIFY_RENEWAL_SUCCESS_ENTEL_PERU_APP(EnumAppTests.DST_ENTEL_PERU_APP, "DST-ENTEL-PERU-APP-SUCCESS-009", "Entel Peru verify renewal test sucessfully", EnumCodeTypeTests.SUCCESS_ACTION),
    VERIFY_RENEWAL_ERROR_ENTEL_PERU_APP(EnumAppTests.DST_ENTEL_PERU_APP, "DST-ENTEL-PERU-APP-ERROR-009", "Entel Peru verify renewal test failure", EnumCodeTypeTests.CATCH_ERROR),


    VERIFY_POST_PAID_MEMBERSHIP_RECEIPT_SUCCESS_ENTEL_PERU_APP(EnumAppTests.DST_ENTEL_PERU_APP, "DST-ENTEL-PERU-APP-SUCCESS-010", "Entel Peru verify post paid membership receipt test sucessfully", EnumCodeTypeTests.SUCCESS_ACTION),
    VERIFY_POST_PAID_MEMBERSHIP_RECEIPT_ERROR_ENTEL_PERU_APP(EnumAppTests.DST_ENTEL_PERU_APP, "DST-ENTEL-PERU-APP-ERROR-010", "Entel Peru verify post paid membership receipt test failure", EnumCodeTypeTests.CATCH_ERROR);


    EnumAppTests app;
    String code;
    String description;

    EnumCodeTypeTests codeType;

    EnumCodeProcessTests(EnumAppTests app, String code, String description, EnumCodeTypeTests codeType) {
        this.app = app;
        this.code = code;
        this.description = description;
        this.codeType = codeType;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public EnumAppTests getApp() {
        return app;
    }

    public EnumCodeTypeTests getCodeType() {
        return codeType;
    }
}
