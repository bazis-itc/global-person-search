package bazis.utils.global_person_search.protocol.rtf;

import bazis.cactoos3.Scalar;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.map.MapEnvelope;
import bazis.cactoos3.scalar.CachedScalar;
import bazis.sitex3.misc.ReportRow;
import bazis.utils.global_person_search.Appoint;
import bazis.utils.global_person_search.printed.PrintedPayouts;
import bazis.utils.global_person_search.printed.PrintedPeriods;
import java.util.Map;

final class RtfAppoint extends MapEnvelope<String, Object> {

    RtfAppoint(final Appoint appoint) {
        super(
            new CachedScalar<>(
                new Scalar<Map<String, Object>>() {
                    @Override
                    public Map<String, Object> value() throws BazisException {
                        return RtfAppoint.map(appoint);
                    }
                }
            )
        );
    }

    private static Map<String, Object> map(Appoint appoint)
        throws BazisException {
        return new ReportRow()
            .withString("msp", appoint.msp())
            .withString("category", appoint.category())
            .withString("child", appoint.child())
            .withString(
                "period", new PrintedPeriods(appoint.periods())
            )
            .withString("status", appoint.status())
            .withString(
                "payments", new PrintedPayouts(appoint.payouts())
            );
    }

}
