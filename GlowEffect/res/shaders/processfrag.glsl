#version 430 core

uniform sampler2D diffuseTex;
uniform float time;

in vec2 UV;

out vec4 gl_FragColor;

const float weights[5] = float [](0.12 , 0.22, .35, 0.22, 0.12);
const vec2 pixelSize = vec2(1/800,1/600);

void main() {
    vec2 values[5];

    values = vec2[]( vec2(0.0, -pixelSize.x * 3),
       vec2(0.0, -pixelSize.x * 2), vec2 (0.0, pixelSize.x),
       vec2 (0.0, pixelSize.x * 2) , vec2 (0.0, pixelSize.x * 3));

    gl_FragColor = texture2D(diffuseTex, UV + 0.010*vec2( sin(time+800*UV.x),cos(time+600*UV.y)));
}