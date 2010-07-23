class FacebookFilters {
	
	// Injected by grails
	def facebookService
	def grailsApplication
	
	def filters = {
		
		// Checking the facebook session
		facebook(controller:"*", action:"*") {
			before = {
				def pair, sig, payload = ""
				def cookieName = "fbs_" + grailsApplication.config.facebook.applicationId
				
				log.debug("Executing facebook filter")
				
				def cookie = request.cookies.find {
					it.name == cookieName
				}
				
				session.facebook = [:] // Without cookie we remove the session data
				if(cookie) {
					cookie.value.split("&").each{
						pair = it.split("=")
						session.facebook[pair[0]] = pair[1].decodeURL()
					}
					
					session.facebook = facebookService.validateSession(session.facebook)
				}
			}
		}
	}
} 