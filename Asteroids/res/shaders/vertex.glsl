#version 430 core

layout(location = 0) in vec3 position;

out vec3 pass_color;

uniform vec3 color;
uniform mat4 transformationMatrix;
uniform mat4 modelMatrix;

void main() {

    gl_Position = transformationMatrix * modelMatrix * vec4(position, 1.0);

    pass_color = color;
}
