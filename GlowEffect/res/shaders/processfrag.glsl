#version 430 core

uniform sampler2D diffuseTex;

in vec2 UV;

out vec4 gl_FragColor;

void main() {
    gl_FragColor = texture2D(diffuseTex, UV.xy);
}