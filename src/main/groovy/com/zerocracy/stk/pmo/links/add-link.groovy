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
package com.zerocracy.stk.pmo.links

import com.jcabi.github.Coordinates
import com.zerocracy.pm.ClaimIn
import com.zerocracy.pmo.Catalog

assume.type('Add link').exact()
assume.roles('PO').exist()

ClaimIn claim = new ClaimIn(xml)
String pid = claim.param('project')
String rel = claim.param('rel')
String href = claim.param('href')
if ('github' == rel) {
  github.repos().get(
    new Coordinates.Simple(href)
  ).stars().star()
}
new Catalog(project).link(pid, rel, href)
claim.reply(
  String.format(
    'The project is linked with rel=`%s` and href=`%s`.',
    rel, href
  )
).postTo(project)
