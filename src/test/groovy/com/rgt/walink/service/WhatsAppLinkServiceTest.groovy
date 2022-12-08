package com.rgt.walink.service

import spock.lang.Specification

class WhatsAppLinkServiceTest extends Specification {

    WhatsAppLinkService service = new WhatsAppLinkService()

    def "should create link from phone number"() {
        when:
        def result = service.createLink(phone)

        then:
        result == link

        where:
        phone               | link
        "+78005553535"      | "https://wa.me/+78005553535"
        "78005553535"       | "https://wa.me/+78005553535"
        "+7 800 555-35-35"  | "https://wa.me/+78005553535"
        "+7(800)555-35-35"  | "https://wa.me/+78005553535"
        "+753 800-55-35-35" | "https://wa.me/+753800553535"
    }

    def "should throw exception when input is incorrect"() {
        when:
        service.createLink(phone)

        then:
        thrown(IllegalArgumentException)

        where:
        phone << ['test', '', '911', '1234567890123456']
    }
}
