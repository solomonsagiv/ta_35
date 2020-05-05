package TEST;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class TextToSpeechConvertor {

	public static void main( String[] args ) {
		TextToSpeechConvertor  text = new TextToSpeechConvertor();
		text.speak( "Shlomi" );
	}

	// Some available voices are (kevin, kevin16, alan)
	private static final String VOICE_NAME_KEVIN = "kevin16";
	private final Voice voice;
	
	public TextToSpeechConvertor() {

		VoiceManager vm = VoiceManager.getInstance();		
		voice = vm.getVoice(VOICE_NAME_KEVIN);
		voice.allocate();
	}

	public void speak(String inputText) {

		if(inputText != null && !inputText.isEmpty()) {
			
			voice.speak(inputText);
		}
	}

}

