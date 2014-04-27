HorsePower
==========

KNOWN ISSUES :+

    - Double jumping as black into bottom corner (0:0) or (32) as a regular piece does not result in new black king at that location, so client doesnt recognize there's a new king when it scans the incorrect board leading to Invalid Move when there's a valid king jump it can make and it doesn't take it

~> update with what you're doing <~

$PLUDE : 
    - Fixed forking logic
    - I left some print statements in from debugging ; you can see that everything is going as it should when the piece double jumps into corner, and the result function is setting that corner to the correct king value so wtf...
    - I'm almost certain everything else is working - you can run it making random non minimax moves to see. I ran it about 40-50 times without fail (not including bottom corner)
