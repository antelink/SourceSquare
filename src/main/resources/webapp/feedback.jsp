<form id="form-feedback" action="<c:url value="/service/add/feedback"/>" method="POST">
	<fieldset>
		<legend>Feedback</legend>
		<p><em>Explanation goes here</em></p>
		<div>
			<label for="feedback-name">Name</label>
			<input id="feedback-name" name="feedback-name" type="text" placeholder="Your Name" required>
			<label for="feedback-email">Email Address</label>
			<input id="feedback-email" name="feedback-email" type="email" placeholder="Your Email Address" required>
			<label for="feedback-message">Message</label>
			<textarea id="feedback-message" name="feedback-message" type="text" placeholder="Your Message" required></textarea>
			<div class="QapTcha"></div>
            <input class="button" type="submit" value="Send">
		</div>
	</fieldset>
</form>
