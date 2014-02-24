package io.segment.android.integrations;

import io.segment.android.errors.InvalidSettingsException;
import io.segment.android.integration.SimpleIntegration;
import io.segment.android.models.Alias;
import io.segment.android.models.EasyJSONObject;
import io.segment.android.models.Props;
import io.segment.android.models.Identify;
import io.segment.android.models.Screen;
import io.segment.android.models.Track;
import io.segment.android.models.Traits;
import io.segment.android.utils.Parameters;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

public class MixpanelIntegration extends SimpleIntegration {

	private static class SettingKey { 

		private static final String TOKEN = "token";
		
		private static final String PEOPLE = "people";
	}
	
	private MixpanelAPI mixpanel;
	
	@Override
	public String getKey() {
		return "Mixpanel";
	}
	
	@Override
	public void validate(EasyJSONObject settings)
			throws InvalidSettingsException {

		if (TextUtils.isEmpty(settings.getString(SettingKey.TOKEN))) {
			throw new InvalidSettingsException(SettingKey.TOKEN, "Mixpanel requires the token setting.");
		}
	}
	
	private boolean isMixpanelPeopleEnabled() {
		EasyJSONObject settings = this.getSettings();
		return settings.getBoolean(SettingKey.PEOPLE, false);
	}
	
	@Override
	public void onCreate(Context context) {

		EasyJSONObject settings = this.getSettings();
		String token = settings.getString(SettingKey.TOKEN);
		
		mixpanel = MixpanelAPI.getInstance(context, token);
		
		ready();
	}
	
	@SuppressWarnings("serial")
	private static final Map<String, String> traitsMapping = new HashMap<String, String>() {
	{
		this.put("created",   "$created");
		this.put("email",     "$email");
		this.put("firstName", "$first_name");
		this.put("lastName",  "$last_name");
		this.put("lastSeen",  "$last_seen");
		this.put("name",      "$name");
		this.put( "username", "$username");
	}};
	
	@Override
	public void identify(Identify identify) {
		String userId = identify.getUserId();
		Traits traits = identify.getTraits();
		
		EasyJSONObject mappedTraits = Parameters.move(traits, traitsMapping);
		
		mixpanel.identify(userId);
		
		if (traits != null)
			mixpanel.registerSuperProperties(mappedTraits);
		
		if (isMixpanelPeopleEnabled()) {
			MixpanelAPI.People people = mixpanel.getPeople();
			people.identify(userId);
			
			@SuppressWarnings("unchecked")
			Iterator<String> it = mappedTraits.keys();
			while (it.hasNext()) {
				String key = it.next();
				people.set(key, mappedTraits.get(key));
			}
		}
	}
	
	@Override
	public void screen(Screen screen) {
		// track a "Viewed SCREEN" event
		track(screen);
	}
	
	@Override
	public void track(Track track) {
		String event = track.getEvent();
		Props properties = track.getProperties();
				
		mixpanel.track(event, properties);
		
		if (isMixpanelPeopleEnabled()) {			
			// consider the charge
			if (properties != null && properties.has("revenue")) {
				MixpanelAPI.People people = mixpanel.getPeople();
				double revenue = properties.getDouble("revenue", 0.0);
				people.trackCharge(revenue, properties);
			}
		}
	}
	
	@Override
	public void alias(Alias alias) {
		String to = alias.getTo();
		
		mixpanel.identify(to);
	}
	
	@Override
	public void reset() {
		
		if (mixpanel != null)
			mixpanel.clearSuperProperties();
	}
	
	@Override
	public void flush() {
		if (mixpanel != null)
			mixpanel.flush();
	}
	
}
