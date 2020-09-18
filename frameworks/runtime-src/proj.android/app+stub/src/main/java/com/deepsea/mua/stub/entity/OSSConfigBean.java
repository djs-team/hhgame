package com.deepsea.mua.stub.entity;

/**
 * 作者：liyaxing  2019/8/26 14:41
 * 类别 ：
 */
public class OSSConfigBean {


    /**
     * SecurityToken : CAIS/QF1q6Ft5B2yfSjIr4nGHdvFqI1tx7vTcGSAt1Q7QOhimZOTljz2IHlEeHJuBeobt/Q/mWxZ6/oTlqJ4T55IQ1Dza8J148zGSvgox8+T1fau5Jko1beXewHKeSOZsebWZ+LmNqS/Ht6md1HDkAJq3LL+bk/Mdle5MJqP+/EFA9MMRVv6F3kkYu1bPQx/ssQXGGLMPPK2SH7Qj3HXEVBjt3gb6wZ24r/txdaHuFiMzg+46JdM/N2qcsT4Npc8ZM8uD4jv5oEsKPqdihw3wgNR6aJ7gJZD/Tr6pdyHCzFTmU7fY7aPrIE1cVYpNvVmSvMY/eKXkuZjqgR1TRttftQnGoABDonKCgOVQx/fNTzafG2Z6p688r7rXUTRqDjCZmUtKMufoRPhxdByfB539p/8AVxzwTP3BCAR5nDy8o0JZDH8jNvpvbiLA7OsSqte3LbwKgsdAlNTXsz2b1J+Rt05zUkQhESYs2qTGsLpGtDFhzYcBNbTELjyxlLpitsMKrzcg64=
     * AccessKeyId : STS.NJsVaqERLpy8rB1STnLgNvV8t
     * AccessKeySecret : Ap4FccSSTfcpo8iVAksbFx5AWUamGvbmXHywTRsYiBo
     * Expiration : http://oss-cn-zhangjiakou.aliyuncs.com
     * Endpoint : 2019-08-26T04:17:47Z
     */
    public String SecurityToken;
    public String AccessKeyId;
    public String AccessKeySecret;
    public String Expiration;
    public String Endpoint;
    public String BucketName;

    @Override
    public String toString() {
        return "OSSConfigBean{" +
                "SecurityToken='" + SecurityToken + '\'' +
                ", AccessKeyId='" + AccessKeyId + '\'' +
                ", AccessKeySecret='" + AccessKeySecret + '\'' +
                ", Expiration='" + Expiration + '\'' +
                ", BucketName='" + BucketName + '\'' +
                '}';
    }
}
