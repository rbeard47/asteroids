package program;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.openal.ALC11.ALC_MONO_SOURCES;
import static org.lwjgl.openal.ALC11.ALC_STEREO_SOURCES;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.libc.LibCStdlib.free;

public class SoundManager {

    private static SoundManager instance;
    private long device;
    private long context;
    private IntBuffer buffers;
    private IntBuffer sources;
    private ALCCapabilities alcCapabilities;
    private ALCapabilities alCapabilities;

    protected SoundManager() {

        buffers = BufferUtils.createIntBuffer(SoundEffect.values().length);
        sources = BufferUtils.createIntBuffer(SoundEffect.values().length);
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }

        return instance;
    }

    public void Init() {

        String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        device = alcOpenDevice(defaultDeviceName);

        int[] attributes = {0};
        context = alcCreateContext(device, attributes);
        alcMakeContextCurrent(context);

        alcCapabilities = ALC.createCapabilities(device);
        alCapabilities = AL.createCapabilities(alcCapabilities);

        for (SoundEffect effect : SoundEffect.values()) {
            LoadSound(effect);
        }
    }

    public void cleanup() {
        alDeleteSources(sources);
        alDeleteBuffers(buffers);
        alcCloseDevice(device);
        alcDestroyContext(context);
    }

    public boolean LoadSound(SoundEffect soundName) {

        int err;

        IntBuffer monoSources = BufferUtils.createIntBuffer(1);
        alcGetIntegerv(device, ALC_MONO_SOURCES, monoSources);

        IntBuffer stereoSources = BufferUtils.createIntBuffer(1);
        alcGetIntegerv(device, ALC_STEREO_SOURCES, stereoSources);

        err = alGetError();

        int channels;
        int sampleRate = 0;
        int format = -1;
        ShortBuffer rawAudioBuffer = null;

        if (err == AL_NO_ERROR) {

            try (MemoryStack stack = stackPush()) {
                IntBuffer channelsBuffer = stack.mallocInt(1);
                IntBuffer sampleRateBuffer = stack.mallocInt(1);

                rawAudioBuffer = stb_vorbis_decode_filename(String.format("res\\wav\\%s.ogg", soundName.soundEffect), channelsBuffer, sampleRateBuffer);

                channels = channelsBuffer.get(0);
                sampleRate = sampleRateBuffer.get(0);
            }

            if (channels == 1) {
                format = AL_FORMAT_MONO16;
            } else if (channels == 2) {
                format = AL_FORMAT_STEREO16;
            }
        }

        err = alGetError();

        if (err == AL_NO_ERROR) {
            buffers.put(soundName.ordinal(), alGenBuffers());

            alBufferData(buffers.get(soundName.ordinal()), format, rawAudioBuffer, sampleRate);

            free(rawAudioBuffer);

            err = alGetError();
        }

        if (err == AL_NO_ERROR) {
            sources.put(soundName.ordinal(), alGenSources());
            alSourcei(sources.get(soundName.ordinal()), AL_BUFFER, buffers.get(soundName.ordinal()));

            err = alGetError();
        }

        if (err == AL_NO_ERROR) {
            alSourcePlay(sources.get(soundName.ordinal()));
            err = alGetError();
        }

        return (err == AL_NO_ERROR);
    }

    public boolean PlaySound(SoundEffect soundName) {

        alGetError();

        alSourcePlay(sources.get(soundName.ordinal()));

        return (alGetError() == AL_NO_ERROR);
    }

    public enum SoundEffect {
        FIRE("fire"),
        THRUST("thrust"),
        LARGEEXPLOSION("bangLarge"),
        MEDIUMEXPLOSION("bangMedium"),
        SMALLEXPLOSION("bangSmall"),
        BEATONE("beat1"),
        BEATTWO("beat2"),
        SAUCERSMALL("saucerSmall"),
        SAUCERBIG("saucerBig"),
        EXTRASHIP("extraShip");

        private String soundEffect;

        private SoundEffect(String soundEffect) {
            this.soundEffect = soundEffect;
        }
    }
}
