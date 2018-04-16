#version 430 core

layout(location = 0) in vec3 position;

out vec3 pass_color;
out vec2 UV;

uniform vec3 color;
uniform mat4 transformationMatrix;
uniform mat4 modelMatrix;

void main() {
    gl_Position = transformationMatrix * modelMatrix * vec4(position, 1.0);
    UV = (position.xy);
    pass_color = vec3(0.3,0.3,0.3);
}
