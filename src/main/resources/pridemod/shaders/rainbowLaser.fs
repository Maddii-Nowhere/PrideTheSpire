//SpriteBatch will use texture unit 0
uniform sampler2D u_texture;
uniform float u_xStart;
uniform float u_xEnd;

//"in" varyings from our vertex shader
varying vec4 v_color;
varying vec2 v_texCoord;

bool compare(float fragX, float colorX) {
    if (u_xStart > u_xEnd) {
        return fragX > colorX;
    } else {
        return fragX < colorX;
    }
}

void main() {
    vec4 texColor = texture2D(u_texture, v_texCoord);

    float step = 1.0 / 7;
    float xRed = mix(u_xStart, u_xEnd, step * 1);
    float xOrange = mix(u_xStart, u_xEnd, step * 2);
    float xYellow = mix(u_xStart, u_xEnd, step * 3);
    float xGreen = mix(u_xStart, u_xEnd, step * 4);
    float xBlue = mix(u_xStart, u_xEnd, step * 5);
    float xIndigo = mix(u_xStart, u_xEnd, step * 6);
    float xViolet = mix(u_xStart, u_xEnd, step * 7);

    if (compare(gl_FragCoord.x, xRed)) {
        gl_FragColor = texColor * vec4(1, 0, 0, v_color.a);
    } else if (compare(gl_FragCoord.x, xOrange)) {
        gl_FragColor = texColor * vec4(1, 0.65, 0, v_color.a);
    } else if (compare(gl_FragCoord.x, xYellow)) {
        gl_FragColor = texColor * vec4(1, 1, 0, v_color.a);
    } else if (compare(gl_FragCoord.x, xGreen)) {
        gl_FragColor = texColor * vec4(0, 1, 0, v_color.a);
    } else if (compare(gl_FragCoord.x, xBlue)) {
        gl_FragColor = texColor * vec4(0, 0, 1, v_color.a);
    } else if (compare(gl_FragCoord.x, xIndigo)) {
        gl_FragColor = texColor * vec4(0.29, 0, 0.5, v_color.a);
    } else if (compare(gl_FragCoord.x, xViolet)) {
        gl_FragColor = texColor * vec4(0.5, 0, 1, v_color.a);
    } else {
        gl_FragColor = texColor * v_color;
    }
}
