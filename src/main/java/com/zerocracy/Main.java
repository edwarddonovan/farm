/**
 * Copyright (c) 2016-2017 Zerocracy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to read
 * the Software only. Permissions is hereby NOT GRANTED to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.zerocracy;

import com.zerocracy.farm.S3Farm;
import com.zerocracy.jstk.Farm;
import com.zerocracy.pmo.ext.ExtBucket;
import com.zerocracy.pmo.ext.ExtFarm;
import com.zerocracy.radars.github.TkGithub;
import com.zerocracy.radars.slack.RrSlack;
import com.zerocracy.radars.slack.TkSlack;
import com.zerocracy.tk.TkAlias;
import com.zerocracy.tk.TkApp;
import com.zerocracy.tk.TkPing;
import java.io.IOException;
import org.takes.facets.fork.FkRegex;
import org.takes.http.Exit;
import org.takes.http.FtCli;

/**
 * Main entry point.
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.1
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
public final class Main {

    /**
     * Command line args.
     */
    private final String[] arguments;

    /**
     * Ctor.
     * @param args Command line arguments
     */
    public Main(final String... args) {
        this.arguments = args;
    }

    /**
     * Main.
     * @param args Command line arguments
     * @throws IOException If fails on I/O
     */
    public static void main(final String... args) throws IOException {
        new Main(args).exec();
    }

    /**
     * Run it.
     * @throws IOException If fails on I/O
     */
    public void exec() throws IOException {
        final Farm farm = new ExtFarm(
            new S3Farm(
                new ExtBucket().asValue()
            )
        ).asValue();
        try (final RrSlack radar = new RrSlack(farm)) {
            radar.refresh();
            new FtCli(
                new TkApp(
                    new FkRegex("/slack", new TkSlack(farm, radar)),
                    new FkRegex("/alias", new TkAlias(farm)),
                    new FkRegex("/ghook", new TkGithub(farm)),
                    new FkRegex("/ping", new TkPing(farm))
                ),
                this.arguments
            ).start(Exit.NEVER);
        }
    }

}
