package wwwordz.shared;

public class Configs {

	public static final long JOIN_STAGE_DURATION = 5000L;
	public static final long PLAY_STAGE_DURATION = 30000L;
	public static final long REPORT_STAGE_DURATION = 5000L;
	public static final long RANKING_STAGE_DURATION = 5000L;
	public static final long SLACK = 200L;

	public Configs() {

	}

	public static final long joinDuration() {
		return JOIN_STAGE_DURATION;
	}

	public static final long playDuration() {
		return PLAY_STAGE_DURATION;
	}

	public static final long reportDuration() {
		return REPORT_STAGE_DURATION;
	}

	public static final long rankingDuration() {
		return RANKING_STAGE_DURATION;
	}

	public static final long roundDuration() {
		return JOIN_STAGE_DURATION + PLAY_STAGE_DURATION + REPORT_STAGE_DURATION + RANKING_STAGE_DURATION;
	}

}
