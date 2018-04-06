#version 430 core

in vec3 pass_color;

out vec4 gl_FragColor;

void main() {

    gl_FragColor = vec4(pass_color, 1.0);
}
