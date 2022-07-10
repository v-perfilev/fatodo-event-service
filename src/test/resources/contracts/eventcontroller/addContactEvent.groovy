package contracts.eventcontroller

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'add contact event'
    description 'should return status 201'
    request {
        method POST()
        url("/api/events/contact")
        headers {
            contentType applicationJson()
            header 'Authorization': $(
                    consumer(containing("Bearer")),
                    producer("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIwMDAwMDAwMC0wMDAwLTAwMDAtMDAwMC0wMDAwMDAwMDAwMDAiLCJ1c2VybmFtZSI6InRlc3Rfc3lzdGVtIiwiYXV0aG9yaXRpZXMiOiJST0xFX1NZU1RFTSIsImlhdCI6MCwiZXhwIjozMjUwMzY3NjQwMH0.roNFKrM7NjEzXvRFRHlJXw0YxSFZ-4Afqvn7eFatpGF14olhXBvCvR9CkPkmlnlCAOYbpDO18krfi6SEX0tQ6Q")
            )
        }
        body(
                "type": $(
                        consumer(anyNonBlankString()),
                        producer("CONTACT_SEND")
                ),
                "recipientIds": $(
                        consumer(any()),
                        producer([anyUuid()])
                ),
                "firstUserId": anyUuid(),
                "secondUserId": anyUuid(),
        )
    }
    response {
        status 201
    }
}
