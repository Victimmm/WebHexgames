package hex;
import java.awt.*;
public interface Player {
	void undoCalled();
	void setTeam(int team);
	Point setMove( Point hex);
	void setDepth(int height);
}