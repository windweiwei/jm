#!/bin/sh
dir=$(cd "$(dirname "$0")"; pwd)
. $dir/sns.sh
shutdown
