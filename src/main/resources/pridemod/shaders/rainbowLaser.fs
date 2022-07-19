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

vec3 RED    = vec3(1, 0, 0);
vec3 ORANGE = vec3(1, 0.65, 0);
vec3 YELLOW = vec3(1, 1, 0);
vec3 GREEN  = vec3(0, 1, 0);
vec3 BLUE   = vec3(0, 0, 1);
vec3 INDIGO = vec3(0.29, 0, 0.5);
vec3 VIOLET = vec3(0.5, 0, 1);

void main() {
    vec4 texColor = texture2D(u_texture, v_texCoord);

    float step = 1.0 / 7;
    float xRed = mix(u_xStart, u_xEnd, step * 0.5);
    float xOrange = mix(u_xStart, u_xEnd, step * 1.5);
    float xYellow = mix(u_xStart, u_xEnd, step * 2.5);
    float xGreen = mix(u_xStart, u_xEnd, step * 3.5);
    float xBlue = mix(u_xStart, u_xEnd, step * 4.5);
    float xIndigo = mix(u_xStart, u_xEnd, step * 5.5);
    float xViolet = mix(u_xStart, u_xEnd, step * 6.5);

    vec3 rainbowColor = v_color.rgb;
    if (compare(gl_FragCoord.x, xRed)) {
        rainbowColor = RED;
    } else if (compare(gl_FragCoord.x, xOrange)) {
        rainbowColor = mix(RED, ORANGE, (gl_FragCoord.x - xRed) / (xOrange - xRed));
    } else if (compare(gl_FragCoord.x, xYellow)) {
        rainbowColor = mix(ORANGE, YELLOW, (gl_FragCoord.x - xOrange) / (xYellow - xOrange));
    } else if (compare(gl_FragCoord.x, xGreen)) {
        rainbowColor = mix(YELLOW, GREEN , (gl_FragCoord.x - xYellow) / (xGreen - xYellow));
    } else if (compare(gl_FragCoord.x, xBlue)) {
        rainbowColor = mix(GREEN, BLUE, (gl_FragCoord.x - xGreen) / (xBlue - xGreen));
    } else if (compare(gl_FragCoord.x, xIndigo)) {
        rainbowColor = mix(BLUE, INDIGO, (gl_FragCoord.x - xBlue) / (xIndigo - xBlue));
    } else if (compare(gl_FragCoord.x, xViolet)) {
        rainbowColor = mix(INDIGO, VIOLET, (gl_FragCoord.x - xIndigo) / (xViolet - xIndigo));
    } else {
        rainbowColor = VIOLET;
    }
    gl_FragColor = texColor * vec4(rainbowColor, v_color.a);
}
