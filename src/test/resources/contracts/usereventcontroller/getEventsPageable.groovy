package contracts.usereventcontroller

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'get events for user'
    description 'should return status 200 and list of UserEventDTO'
    request {
        method GET()
        url("/api/user-events")
        headers {
            header 'Authorization': $(
                    consumer(containing("Bearer")),
                    producer("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI4ZjlhN2NhZS03M2M4LTRhZDYtYjEzNS01YmQxMDliNTFkMmUiLCJ1c2VybmFtZSI6InRlc3RfdXNlciIsImF1dGhvcml0aWVzIjoiUk9MRV9VU0VSIiwiaWF0IjowLCJleHAiOjMyNTAzNjc2NDAwfQ.Go0MIqfjREMHOLeqoX2Ej3DbeSG7ZxlL4UAvcxqNeO-RgrKUCrgEu77Ty1vgR_upxVGDAWZS-JfuSYPHSRtv-w")
            )
        }
    }
    response {
        status 200
        headers {
            contentType applicationJson()
        }
        body(
                "data": [
                        "type"        : anyNonBlankString(),
                        "contactEvent": optional(any()),
                        "itemEvent"   : optional(any()),
                        "commentEvent": optional(any()),
                        "chatEvent"   : optional(any()),

                ],
                "count": anyNumber(),
                "unread": anyNumber()
        )
    }
}
